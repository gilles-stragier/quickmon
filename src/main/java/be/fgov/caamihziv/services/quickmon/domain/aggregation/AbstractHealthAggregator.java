package be.fgov.caamihziv.services.quickmon.domain.aggregation;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * Created by gs on 24.04.17.
 */
public abstract class AbstractHealthAggregator implements HealthAggregator {

    private Duration window;
    private List<HealthStatus> statuses;

    protected AbstractHealthAggregator(Duration window) {
        this.window = window;
        this.statuses = new ArrayList<HealthStatus>();
    }

    @Override
    public List<HealthStatus> getHistory() {
        return statuses;
    }

    @Override
    public Duration getWindow() {
        return window;
    }

    @Override
    public HealthStatus computeHealth(HealthStatus status) {
        statuses.add(status);
        cleanOldStatuses();
        return doComputeHealth(status);
    }

    protected void cleanOldStatuses() {
        LocalDateTime lowerBound = LocalDateTime.now().minus(window);
        List<HealthStatus> outdated = statuses.stream().filter(status -> status.getCheckedOn().isBefore(lowerBound)).collect(Collectors.toList());
        statuses.removeAll(outdated);
    }

    protected abstract HealthStatus doComputeHealth(HealthStatus status);

    @Override
    public Map<HealthStatus.Health, Integer> getStats() {
        Map<HealthStatus.Health, Integer> stats = new HashMap<>();

        stream(HealthStatus.Health.values()).forEach(health -> {
            Long count = statuses.stream().filter(status -> status.getHealth().equals(health)).count();
            if (statuses.size() > 0) {
                stats.put(health, (count.intValue() * 100) / statuses.size());
            } else {
                stats.put(health, 0);
            }
        });
        return stats;
    }

    @Override
    public String getType() {
        return toBuilder().getType();
    }
}
