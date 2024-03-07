package com.monitoring.application.rule.model;

import jakarta.persistence.*;

import java.util.Objects;

@Table(name = "rules")
@Entity(name = "rules")
public class Rule {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="rule_seq")
    @SequenceGenerator(name="rule_seq", sequenceName="RULE_SEQ", allocationSize=1)
    private Long id;
    @Column(nullable = false, name = "url", unique = true)
    private String URL;
    @Column(nullable = false, name = "millis_interval")
    private Long millisInterval;
    @Column(nullable = false)
    private Short expectedStatus;
    @Column(nullable = false)
    private Boolean enabled = true;
    @Column(nullable = false)
    private Short lastTestStatus;

    public Rule() {
    }

    public Rule(String URL, Long millisInterval, Short expectedStatus) {
        this.URL = URL;
        this.millisInterval = millisInterval;
        this.expectedStatus = expectedStatus;
        this.lastTestStatus = expectedStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Long getMillisInterval() {
        return millisInterval;
    }

    public void setMillisInterval(Long millisInterval) {
        this.millisInterval = millisInterval;
    }

    public Short getExpectedStatus() {
        return expectedStatus;
    }

    public void setExpectedStatus(Short expectedStatus) {
        this.expectedStatus = expectedStatus;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Short getLastTestStatus() {
        return lastTestStatus;
    }

    public void setLastTestStatus(Short lastTestStatus) {
        this.lastTestStatus = lastTestStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(id, rule.id) && Objects.equals(URL, rule.URL) && Objects.equals(millisInterval, rule.millisInterval) && Objects.equals(expectedStatus, rule.expectedStatus) && Objects.equals(enabled, rule.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, URL, millisInterval, expectedStatus, enabled);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", URL='" + URL + '\'' +
                ", millisInterval=" + millisInterval +
                ", expectedStatus=" + expectedStatus +
                ", enabled=" + enabled +
                '}';
    }
}
