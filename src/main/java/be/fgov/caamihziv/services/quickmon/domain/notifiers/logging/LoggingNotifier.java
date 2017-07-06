package be.fgov.caamihziv.services.quickmon.domain.notifiers.logging;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.AbstractNotifier;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.Notifier;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.NotifierBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by gs on 08.06.17.
 */
public class LoggingNotifier extends AbstractNotifier {

    private Logger logger = LoggerFactory.getLogger(LoggingNotifier.class);

    public LoggingNotifier(LoggingNotifierBuilder builder) {
        super(builder);
    }

    @Override
    public LoggingNotifierBuilder toBuilder() {
        return new LoggingNotifierBuilder()
                .name(getName())
                .period(getPeriod())
                .statuses(getStatuses())
                .tags(getTags())
                ;
    }

    @Override
    public void doRun(Collection<HealthCheck> healthChecks) {
        if (healthChecks == null || healthChecks.isEmpty()) {
            logger.info("Everything is ok");
        } else {
            logger.info("Problems found : " + healthChecks.size() + " healthchecks have an incorrect health");
            ArrayList<HealthCheck> sortedHealthChecks = new ArrayList<>(healthChecks);
            Collections.sort(sortedHealthChecks, (o1, o2) -> o1.getName().compareTo(o2.getName()));
            sortedHealthChecks.stream().forEach(hc -> {
                logger.info(String.format("%s (%s) - %s", hc.getName(), hc.getLastStatus().getHealth(), hc.getLastStatus().getMessage()));
            });
        }
    }
}
