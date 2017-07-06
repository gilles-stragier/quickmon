package be.fgov.caamihziv.services.quickmon.domain.aggregation;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Created by gs on 24.04.17.
 */
public interface HealthAggregator {
    HealthStatus computeHealth(HealthStatus status);
    Duration getWindow();
    Map<HealthStatus.Health, Integer> getStats();
    List<HealthStatus> getHistory();
    AggregatorBuilder toBuilder();
    String getType();
}
