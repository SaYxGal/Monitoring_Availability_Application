package com.monitoring.application.rule.repository;

import com.monitoring.application.rule.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository  extends JpaRepository<Rule, Long> {
}
