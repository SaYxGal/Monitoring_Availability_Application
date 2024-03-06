package com.monitoring.application.rule.service;

import com.monitoring.application.rule.model.DTO.CreateRuleDTO;
import com.monitoring.application.rule.model.Rule;

import java.util.List;

public interface RuleService {
    List<Rule> getAllRules();
    Rule findRule(Long id);
    Boolean createRule(CreateRuleDTO ruleInfo);
    Boolean deleteRule(Long id);
    String turnOnOffRule(Long id);
}
