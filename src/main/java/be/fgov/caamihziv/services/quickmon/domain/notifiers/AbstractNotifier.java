package be.fgov.caamihziv.services.quickmon.domain.notifiers;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckRepository;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Created by gs on 08.06.17.
 */
public abstract class AbstractNotifier implements Notifier{

    private String name;
    private Duration period;
    private LocalDateTime lastTimeRun = null;
    private Collection<String> tags;
    private Collection<HealthStatus.Health> statuses;
    private boolean onlyOnChange;
    private Object lastRunChecksum;

    public AbstractNotifier (NotifierBuilder builder) {
        this.name = builder.getName();
        this.period = builder.getPeriod();
        this.tags = builder.getTags();
        this.statuses = builder.getStatuses();
        this.onlyOnChange = builder.isOnlyOnChange();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Duration getPeriod() {
        return period;
    }

    @Override
    public LocalDateTime getLastTimeRun() {
        return lastTimeRun;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public Collection<HealthStatus.Health> getStatuses() {
        return statuses;
    }

    @Override
    public boolean shouldRun() {
        return lastTimeRun == null || lastTimeRun.plus(period).isBefore(LocalDateTime.now());
    }

    @Override
    public void run(HealthCheckRepository healthCheckRepository) {

        Collection<HealthCheck> all = healthCheckRepository.findAll(
                healthCheck -> statuses.contains(healthCheck.getLastStatus().getHealth()) && healthCheck.getTags().containsAll(tags)
        );

        if (onlyOnChange) {
            // Here we'll try to determine if something has changed since last notification
            String newCheckSum = computeCheckSum(all);
            if (!newCheckSum.equals(lastRunChecksum)) {
                doRun(all);
                lastTimeRun = LocalDateTime.now();
                lastRunChecksum = newCheckSum;
            }
        } else {
            doRun(all);
            lastTimeRun = LocalDateTime.now();
        }


    }

    public String computeCheckSum(Collection<HealthCheck> healthChecks) {
        StringBuilder builder = new StringBuilder();
        healthChecks.stream().forEach(healthCheck -> {
            builder.append(healthCheck.getLastStatus().getHealth());
        });
        return builder.toString();
    }

    public abstract void doRun(Collection<HealthCheck> healthChecks);
}
