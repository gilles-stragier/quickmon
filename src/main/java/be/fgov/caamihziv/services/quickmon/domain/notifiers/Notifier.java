package be.fgov.caamihziv.services.quickmon.domain.notifiers;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckRepository;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by gs on 08.06.17.
 */
public interface Notifier {

    abstract String getName();

    Duration getPeriod();

    LocalDateTime getLastTimeRun();

    boolean shouldRun();

    void run(HealthCheckRepository healthCheckRepository);

    <R extends NotifierBuilder> R toBuilder();
}
