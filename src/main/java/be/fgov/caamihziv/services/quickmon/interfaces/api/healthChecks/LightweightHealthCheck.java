package be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;
import org.springframework.hateoas.ResourceSupport;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * Created by gs on 16.04.17.
 */
public class LightweightHealthCheck extends ResourceSupport {

    private HealthCheck healthCheck;

    public LightweightHealthCheck(HealthCheck healthCheck) {
        this.healthCheck = healthCheck;
    }

    public HealthStatus.Health getLastHealth() {
        return healthCheck.getLastStatus().getHealth();
    }

    public String getName() {
        return healthCheck.getName();
    }

    public Set<String> getTags() {
        return healthCheck.getTags();
    }

    public String getFa() {
        return healthCheck.getFa();
    }

    public Map<HealthStatus.Health, Integer> getStats() {
        return healthCheck.getAggregator().getStats();
    }

    public LocalDateTime getCheckedOn() {
        return  healthCheck.getLastStatus().getCheckedOn();
    }

    public String getMessage() {
        return healthCheck.getLastStatus().getMessage();
    }



}
