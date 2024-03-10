package com.monitoring.application.rule.controller;

import com.monitoring.application.configuration.OpenApi30Configuration;
import com.monitoring.application.rule.model.DTO.CreateRuleDTO;
import com.monitoring.application.rule.model.DTO.ViewRuleDTO;
import com.monitoring.application.rule.model.Rule;
import com.monitoring.application.rule.service.RuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Tag(name = "Правила", description = "API для правил")
@RestController
@RequestMapping(OpenApi30Configuration.API_PREFIX + "/rules")
public class RuleController {
    @Autowired
    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }
    @Operation(
            summary = "Создать правило")
    @PostMapping(value = "")
    public ResponseEntity<?> create(@RequestBody @Valid CreateRuleDTO ruleInfo) {
        ruleService.createRule(ruleInfo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Operation(
            summary = "Получить все правила")
    @GetMapping(value = "")
    public ResponseEntity<List<ViewRuleDTO>> readAll() {
        final List<Rule> rules = ruleService.getAllRules();
        return rules != null
                ? new ResponseEntity<>(rules.stream().map(ViewRuleDTO::new).toList(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Operation(
            summary = "Включить/выключить правило")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Map<String, String>> updateEnabledState(@PathVariable(name = "id") long id) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ruleService.turnOnOffRule(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(
            summary = "Удалить правило")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        final boolean deleted = ruleService.deleteRule(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
