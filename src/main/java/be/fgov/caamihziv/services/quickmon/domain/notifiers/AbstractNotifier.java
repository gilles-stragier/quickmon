package be.fgov.caamihziv.services.quickmon.domain.notifiers;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckRepository;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronSequenceGenerator;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

/**
 * Created by gs on 08.06.17.
 */
public abstract class AbstractNotifier implements Notifier{

    private static Logger logger = LoggerFactory.getLogger(AbstractNotifier.class);

    private String name;
    private Optional<Duration> period;
    private Optional<String> schedulingCronExpression;
    private LocalDateTime lastTimeRun = null;
    private Collection<String> tags;
    private Collection<HealthStatus.Health> statuses;
    private boolean onlyOnChange;
    private Object lastRunChecksum;
    private LocalDateTime createdOn;

    public AbstractNotifier (NotifierBuilder builder) {
        this.name = builder.getName();
        this.period = Optional.ofNullable(builder.getPeriod());
        this.schedulingCronExpression = Optional.ofNullable(builder.getSchedulingCronExpression());
        this.tags = builder.getTags();
        this.statuses = builder.getStatuses();
        this.onlyOnChange = builder.isOnlyOnChange();
        this.createdOn = builder.getCreatedOn();
    }

    @Override
    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    @Override
    public Duration getPeriod() {
        return period != null ? period.orElse(null) : null;
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

    public String getSchedulingCronExpression() {
        return schedulingCronExpression != null ? schedulingCronExpression.orElse(null) : null;
    }

    @Override
    public boolean shouldRun() {
        return period.map(period -> {
            return lastTimeRun == null || lastTimeRun.plus(period).isBefore(LocalDateTime.now());
        }).orElseGet(() -> {
            CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(schedulingCronExpression.get());
            Date pivotDate = lastTimeRun != null ? Date.from(lastTimeRun.atZone(ZoneOffset.systemDefault()).toInstant()) : Date.from(createdOn.atZone(ZoneOffset.systemDefault()).toInstant());
            boolean shouldRun = cronSequenceGenerator.next(pivotDate).before(now());
            if (shouldRun) {
                logger.info("Notification " + getName() + " with cron " + getSchedulingCronExpression() + " and pivot " + pivotDate + " should run now");
            }
            return shouldRun;
        });
    }

    protected Date now() {
        return new Date();
    }

    @Override
    public void run(HealthCheckRepository healthCheckRepository) {

        Collection<HealthCheck> all = healthCheckRepository.findAll(
                healthCheck -> statuses.contains(healthCheck.getLastStatus().getHealth()) && healthCheck.getTags().containsAll(tags)
        );

        if (all == null || all.isEmpty()) {
            return;
        }

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
            builder.append(healthCheck.getName()).append(":");
            builder.append(healthCheck.getLastStatus().getHealth()).append(".");
        });
        return builder.toString();
    }

    public abstract void doRun(Collection<HealthCheck> healthChecks);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AbstractNotifier that = (AbstractNotifier) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }
}
