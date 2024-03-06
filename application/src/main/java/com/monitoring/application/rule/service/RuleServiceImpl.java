package com.monitoring.application.rule.service;

import com.monitoring.application.rule.model.DTO.CreateRuleDTO;
import com.monitoring.application.rule.model.Rule;
import com.monitoring.application.rule.repository.RuleRepository;
import com.monitoring.application.rule.service.exception.RuleNotFoundException;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class RuleServiceImpl implements RuleService {
    private final RuleRepository ruleRepository;

    public RuleServiceImpl(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    public List<Rule> getAllRules() {
        return ruleRepository.findAll(Sort.by("id").descending());
    }

    @Override
    public Rule findRule(Long id) {
        final Optional<Rule> rule = ruleRepository.findById(id);
        return rule.orElseThrow(() -> new RuleNotFoundException(id));
    }

    @Override
    public Boolean createRule(CreateRuleDTO ruleInfo) {
        final Rule rule = new Rule(ruleInfo.getURL(), ruleInfo.getMillisInterval(),
                ruleInfo.getExpectedStatus());
        ruleRepository.save(rule);
        return true;
    }

    @Override
    public Boolean deleteRule(Long id) {
        final Rule ruleForDelete = findRule(id);
        ruleRepository.delete(ruleForDelete);
        return true;
    }

    @Override
    public String turnOnOffRule(Long id) {
        final Rule currentRule = findRule(id);
        currentRule.setEnabled(!currentRule.getEnabled());
        ruleRepository.save(currentRule);
        return currentRule.getEnabled() ? String.format("Enabled rule with Id: %d", currentRule.getId())
                : String.format("Disabled rule with Id: %d", currentRule.getId());
    }
}
