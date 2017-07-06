package be.fgov.caamihziv.services.quickmon.domain.healthchecks;

import be.fgov.caamihziv.services.quickmon.domain.Builder;
import be.fgov.caamihziv.services.quickmon.domain.aggregation.AggregatorBuilder;
import be.fgov.caamihziv.services.quickmon.domain.aggregation.mostrecent.MostRecentAggregatorBuilder;
import be.fgov.caamihziv.services.quickmon.domain.assertions.AssertionBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.SamplerBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static be.fgov.caamihziv.services.quickmon.domain.ValidationUtils.notEmpty;
import static be.fgov.caamihziv.services.quickmon.domain.ValidationUtils.notNull;

/**
 * Created by gs on 19.04.17.
 */
public class HealthCheckBuilder implements Builder<HealthCheckBuilder, HealthCheck> {

    @JsonProperty
    private String name;

    @JsonProperty
    private Set<String> tags;
    
    @JsonProperty
    private String fa;

    private List<AssertionBuilder> assertions;

    private AggregatorBuilder aggregator;

    private SamplerBuilder sampler;

    @JsonProperty
    private Long timeoutMillis;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration period;

    public HealthCheckBuilder() {
        this.tags = new HashSet<>();
        this.assertions = new ArrayList<>();
        this.aggregator = new MostRecentAggregatorBuilder();
        this.period = Duration.ofMinutes(1);
        this.timeoutMillis = 1000L;
        this.fa = "fa-question";
                
    }


    public HealthCheckBuilder fa(String val) {
        this.fa = val;
        return this;
    }
    
    public HealthCheckBuilder period(Duration val) {
        this.period = val;
        return this;
    }

    public HealthCheckBuilder tags(Set<String> tags) {
        this.tags = tags;
        return this;
    }
    
    public HealthCheckBuilder assertions(List<AssertionBuilder> assertions) {
        this.assertions = assertions;
        return this;
    }

    public HealthCheckBuilder name(String name) {
        this.name = name;
        return this;
    }

    public HealthCheckBuilder timeoutMillis(Long val) {
        this.timeoutMillis= timeoutMillis;
        return this;
    }

    public HealthCheckBuilder aggregatorBuilder(AggregatorBuilder val) {
        this.aggregator = val;
        return this;
    }

    public HealthCheckBuilder samplerBuilder(SamplerBuilder val) {
        this.sampler = val;
        return this;
    }

    public String getFa() {
        return fa;
    }
    
    public String getName() {
        return name;
    }

    public AggregatorBuilder getAggregator() {
        return aggregator;
    }

    public Duration getPeriod() {
        return period;
    }

    public List<AssertionBuilder> getAssertions() {
        return assertions;
    }

    public Long getTimeoutMillis() {
        return timeoutMillis;
    }

    public Set<String> getTags() {
        return tags;
    }

    public SamplerBuilder getSampler() {
        return sampler;
    }

    @Override
    public List<String> validate() {
        ArrayList<String> errors = new ArrayList<>();

        notEmpty(errors, assertions, "Please provide at least one assertion");
        notNull(errors, aggregator, "Please provide an aggregator as it is mandatory");
        notNull(errors, sampler, "Please provide a sampler as it is mandatory");
            
        if (errors.isEmpty()) {
            errors.addAll(aggregator.validate());
            errors.addAll(sampler.validate());
            assertions.stream().forEach(a -> errors.addAll(a.validate()));
        }

        return errors;
    }


    public HealthCheck build() {
        List<String> errors = validate();
        if (errors.isEmpty()) {
            return new DefaultHealthCheck(this);
        } else {
            throw new IllegalArgumentException("Can't build the health check as we have errors : " + errors);
        }

    }


}
