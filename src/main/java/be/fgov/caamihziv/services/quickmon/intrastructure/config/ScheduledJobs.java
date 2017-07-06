package be.fgov.caamihziv.services.quickmon.intrastructure.config;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckRepository;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.NotifiersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by gs on 15.04.17.
 */
@EnableScheduling
@Configuration
public class ScheduledJobs {

    @Autowired
    private HealthCheckRepository healthCheckRepository;

    @Autowired
    private NotifiersRepository notifiersRepository;

    @Scheduled(fixedDelay = 10000)
    public void refreshAllChecks() {
        healthCheckRepository.findAll().parallelStream().forEach(healthCheck -> healthCheck.doCheck());
    }

    @Scheduled(fixedDelay = 60000, initialDelay = 5000)
    public void notifiers() {
        notifiersRepository.findAll(noti -> noti.shouldRun()).forEach(notifier -> notifier.run(healthCheckRepository));
    }

}
