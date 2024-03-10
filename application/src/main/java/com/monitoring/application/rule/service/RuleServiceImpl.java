package com.monitoring.application.rule.service;

import com.monitoring.application.channel.model.Channel;
import com.monitoring.application.channel.service.ChannelService;
import com.monitoring.application.rule.model.DTO.CreateRuleDTO;
import com.monitoring.application.rule.model.Rule;
import com.monitoring.application.rule.repository.RuleRepository;
import com.monitoring.application.rule.service.exception.RuleNotFoundException;
import com.monitoring.application.telegram.NotificationBot;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Service
@Primary
public class RuleServiceImpl implements RuleService {
    private final RuleRepository ruleRepository;
    private final ChannelService channelService;
    private final NotificationBot notificationBot;
    private final WebClient webClient;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks =
            new HashMap<>();
    private final TaskScheduler taskScheduler;
    public RuleServiceImpl(RuleRepository ruleRepository, @Autowired ChannelService channelService,
                           NotificationBot notificationBot,
                           @Autowired WebClient webClient, @Autowired TaskScheduler taskScheduler) {
        this.ruleRepository = ruleRepository;
        this.channelService = channelService;
        this.notificationBot = notificationBot;
        this.webClient = webClient;
        this.taskScheduler = taskScheduler;
    }
    class ScheduledTaskExecutor implements Runnable{
        private final Long ruleId;
        public ScheduledTaskExecutor(Long ruleId){
            this.ruleId = ruleId;
        }
        private void sendAllListeners(String message){
            for (Channel channel : channelService.getAllChannels()) {
                notificationBot.sendMessageToChannel(channel.getChatId(), message);
            }
        }
        @Override
        @Transactional
        public void run() {
            boolean needSave = false;
            Rule rule;
            try{
                rule = findRule(ruleId);
            }
            catch(Exception ex){
                return;
            }
            try{
               Mono<HttpStatusCode>statusCode = webClient
                       .mutate()
                       .baseUrl(rule.getURL())
                       .build()
                       .method(HttpMethod.GET)
                        .exchangeToMono(clientResponse -> Mono.just(clientResponse.statusCode()));
               int status = statusCode.block().value();
               if (rule.getLastTestStatus() != status) {
                   if(status != rule.getExpectedStatus()){
                       sendAllListeners(String.format("Сервер по пути '%s' вернул некорректный ответ." +
                                       "Ожидаемый ответ - '%d', текущий ответ - '%d'", rule.getURL(),
                               rule.getExpectedStatus(), status));
                   }
                   else{
                       sendAllListeners(String.format("Сервер по пути '%s' вернул корректный ответ." +
                                       "Предыдущий ответ - '%d', текущий ответ - '%d'", rule.getURL(),
                               rule.getLastTestStatus(), status));
                   }
                   rule.setLastTestStatus((short)status);
                   needSave = true;
               }
            }
            catch (Exception e) {
                if (rule.getLastTestStatus() != 503) {
                    rule.setLastTestStatus((short) 503);
                    needSave = true;
                    sendAllListeners(String.format("Сервер по пути '%s' недоступен", rule.getURL()));
                }
            }
            finally {
                if(needSave){
                    ruleRepository.save(rule);
                }
            }
        }
    }
    @PostConstruct
    private void runTasks(){
        List<Rule> rules = getAllRules().stream().filter(Rule::getEnabled).toList();
        for (Rule rule : rules) {
            startScheduledTask(rule);
        }
    }
    private void startScheduledTask(Rule rule){
        scheduledTasks.put(rule.getId(),
                taskScheduler.scheduleWithFixedDelay(new ScheduledTaskExecutor(rule.getId()),
                        Duration.of(rule.getMillisInterval(), ChronoUnit.MILLIS)));
    }
    private void stopScheduledTask(Long ruleId){
        scheduledTasks.get(ruleId).cancel(true);
        scheduledTasks.remove(ruleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rule> getAllRules() {
        return ruleRepository.findAll(Sort.by("id").descending());
    }

    @Override
    @Transactional(readOnly = true)
    public Rule findRule(Long id) {
        final Optional<Rule> rule = ruleRepository.findById(id);
        return rule.orElseThrow(() -> new RuleNotFoundException(id));
    }

    @Override
    @Transactional
    public void createRule(CreateRuleDTO ruleInfo) {
        final Rule rule = new Rule(ruleInfo.getURL(), ruleInfo.getMillisInterval(),
                ruleInfo.getExpectedStatus());
        final Rule savedRule = ruleRepository.save(rule);
        startScheduledTask(savedRule);
    }

    @Override
    @Transactional
    public Boolean deleteRule(Long id) {
        try {
            ruleRepository.deleteById(id);
            stopScheduledTask(id);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    @Override
    @Transactional
    public String turnOnOffRule(Long id) {
        final Rule currentRule = findRule(id);
        currentRule.setEnabled(!currentRule.getEnabled());
        ruleRepository.save(currentRule);
        if(currentRule.getEnabled()){
            startScheduledTask(currentRule);
            return String.format("Enabled rule with Id: %d", currentRule.getId());
        }
        else{
            stopScheduledTask(currentRule.getId());
            return String.format("Disabled rule with Id: %d", currentRule.getId());
        }
    }
}
