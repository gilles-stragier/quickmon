package be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckBuilder;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by gs on 16.04.17.
 */
public class FullHealthCheck extends ResourceSupport {

    private HealthCheck healthCheck;

    public FullHealthCheck(HealthCheck healthCheck) {
        this.healthCheck = healthCheck;
    }

    public Map<HealthStatus.Health, Integer> getStats () {
        return healthCheck.getAggregator().getStats();
    }

    public List<HealthStatus> getHistory() {
        return healthCheck.getAggregator().getHistory();
    }

    public HealthStatus getLastStatus() {
        return healthCheck.getLastStatus();
    }

    public String getName() {
        return healthCheck.getName();
    }

    public Set<String> getTags() {
        return healthCheck.getTags();
    }

    public HealthCheckBuilder getDefinition() {
        return healthCheck.toBuilder();
    }

}
