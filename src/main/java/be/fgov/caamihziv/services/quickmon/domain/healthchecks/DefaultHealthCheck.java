package be.fgov.caamihziv.services.quickmon.domain.healthchecks;

import be.fgov.caamihziv.services.quickmon.domain.aggregation.HealthAggregator;
import be.fgov.caamihziv.services.quickmon.domain.assertions.Assertion;
import be.fgov.caamihziv.services.quickmon.domain.samplers.Sampler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gs on 15.04.17.
 */
public class DefaultHealthCheck implements HealthCheck {

    private Set<String> tags;
    private HealthStatus lastStatus;
    private String name;
    private List<Assertion> assertions;
    private HealthAggregator aggregator;
    private Sampler sampler;
    private Duration period;
    private Long timeoutMillis;
    private String fa;

    public DefaultHealthCheck(HealthCheckBuilder builder) {
        this.tags = builder.getTags();
        this.name = builder.getName();
        assertions = builder.getAssertions().stream().map(oneBuilder -> oneBuilder.build()).collect(Collectors.toList());
        aggregator = builder.getAggregator().build();
        this.sampler = builder.getSampler().build();
        this.timeoutMillis = builder.getTimeoutMillis();
        this.period = builder.getPeriod();
        this.fa = builder.getFa();
        this.lastStatus = new HealthStatus(HealthStatus.Health.UNCHECKED, "Yet to be checked");
    }

    @Override
    public HealthStatus getLastStatus() {
        return lastStatus;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> getTags() {
        return tags;
    }

    protected boolean shouldCheck() {
        return lastStatus == null || lastStatus.getHealth().equals(HealthStatus.Health.UNCHECKED) || lastStatus.getCheckedOn().plus(period).isBefore(LocalDateTime.now());
    }

    @Override
    public HealthStatus doCheck() {
        if (shouldCheck()) {
            try {
                Object sample = sample();
                HealthStatus status = computeStatusFromAssertions(sample);
                lastStatus = aggregator.computeHealth(status);
            } catch (Exception e) {
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                lastStatus = aggregator.computeHealth(new HealthStatus(HealthStatus.Health.CRITICAL, errors.toString().split("\n"), "Exception : " + e.getMessage()));
            }
        }
        return lastStatus;
    }

    private HealthStatus computeStatusFromAssertions(Object sample) {
        Map<Assertion, Assertion.Result> results = new HashMap<>();

        assertions.stream().forEach( a ->
                results.put(a, a.execute(sample))
        );

        return results.values().stream().filter(r -> !r.isSuccess()).findFirst().map(result -> {
                if (result.isCritical()) {
                    return new HealthStatus(HealthStatus.Health.CRITICAL, sample, "Assertion failed : " + result.getMessage(), results);
                } else {
                    return new HealthStatus(HealthStatus.Health.WARNING, sample, "Assertion failed : " + result.getMessage(), results);
                }
        }).orElse(new HealthStatus(HealthStatus.Health.OK, "All assertions are correct"));
    }

    public Object sample() {
        return sampler.sample();
    }

    public HealthCheckBuilder toBuilder() {
        return new HealthCheckBuilder()
                .assertions(assertions.stream().map(assertion -> assertion.toBbuilder()).collect(Collectors.toList()))
                .name(getName())
                .period(getPeriod())
                .timeoutMillis(getTimeoutMillis())
                .tags(getTags())
                .samplerBuilder(sampler.toBuilder())
                .aggregatorBuilder(aggregator.toBuilder())
                .fa(getFa())
                ;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DefaultHealthCheck that = (DefaultHealthCheck) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(name, that.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }

    public Duration getPeriod() {
        return period;
    }

    public Long getTimeoutMillis() {
        return timeoutMillis;
    }

    public List<Assertion> getAssertions() {
        return assertions;
    }

    @Override
    public HealthAggregator getAggregator() {
        return aggregator;
    }

    @Override
    public Sampler getSampler() {
        return sampler;
    }

    public String getFa() {
        return fa;
    }
}
