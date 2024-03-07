package com.monitoring.application.rule.service;

import com.monitoring.application.rule.model.DTO.CreateRuleDTO;
import com.monitoring.application.rule.model.Rule;
import com.monitoring.application.rule.repository.RuleRepository;
import com.monitoring.application.rule.service.exception.RuleNotFoundException;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

@Service
@Primary
public class RuleServiceImpl implements RuleService {
    private final RuleRepository ruleRepository;
    private final WebClient webClient;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks =
            new IdentityHashMap<>();
    private final TaskScheduler taskScheduler;
    class ScheduledTaskExecutor implements Runnable{
        private final Rule rule;
        public ScheduledTaskExecutor(Rule rule){
            this.rule = rule;
        }
        @Override
        public void run() {
            try{
               Mono<HttpStatusCode>statusCode = webClient
                        .method(HttpMethod.GET)
                       .uri(rule.getURL())
                        .exchangeToMono(clientResponse -> Mono.just(clientResponse.statusCode()));
               int status = statusCode.block().value();
               if (rule.getLastTestStatus() != status) {
                   if(status != rule.getExpectedStatus()){
                       System.out.println("System is abnormal now. From " + rule.getLastTestStatus() + "to " + status + " "
                               + Instant.now() + " " + Thread.currentThread().getName());
                   }
                   else{
                       System.out.println("System is ok now. From " + rule.getLastTestStatus() + "to " + status + " "
                               + Instant.now() + " " + Thread.currentThread().getName());
                   }
               }
               rule.setLastTestStatus((short)status);;
            }
            catch (Exception e) {
                rule.setLastTestStatus((short) 503);
                System.out.println("System is not accessible");
            }
            finally {
                ruleRepository.save(rule);
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
                taskScheduler.scheduleWithFixedDelay(new ScheduledTaskExecutor(rule),
                        Duration.of(rule.getMillisInterval(), ChronoUnit.MILLIS)));
    }
    private void stopScheduledTask(Long ruleId){
        scheduledTasks.get(ruleId).cancel(false);
        scheduledTasks.remove(ruleId);
    }

    public RuleServiceImpl(RuleRepository ruleRepository, @Autowired WebClient webClient, @Autowired TaskScheduler taskScheduler) {
        this.ruleRepository = ruleRepository;
        this.webClient = webClient;
        this.taskScheduler = taskScheduler;
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
        final Rule ruleForDelete = findRule(id);
        ruleRepository.delete(ruleForDelete);
        stopScheduledTask(ruleForDelete.getId());
        return true;
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
