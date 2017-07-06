package be.fgov.caamihziv.services.quickmon.domain.healthchecks;

import be.fgov.caamihziv.services.quickmon.domain.aggregation.HealthAggregator;
import be.fgov.caamihziv.services.quickmon.domain.assertions.Assertion;
import be.fgov.caamihziv.services.quickmon.domain.samplers.Sampler;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;

/**
 * Created by gs on 15.04.17.
 */

public interface HealthCheck {

    HealthStatus getLastStatus();
    String getName();
    Set<String> getTags();
    HealthStatus doCheck();

    @JsonProperty("definition")
    HealthCheckBuilder toBuilder();
    Sampler getSampler();
    HealthAggregator getAggregator();
    List<Assertion> getAssertions();
    String getFa();
}
