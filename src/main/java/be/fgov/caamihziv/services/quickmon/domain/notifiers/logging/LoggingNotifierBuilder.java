package be.fgov.caamihziv.services.quickmon.domain.notifiers.logging;

import be.fgov.caamihziv.services.quickmon.domain.notifiers.NotifierBuilder;

/**
 * Created by gs on 12.06.17.
 */
public class LoggingNotifierBuilder  extends NotifierBuilder<LoggingNotifierBuilder, LoggingNotifier> {

    @Override
    public LoggingNotifier build() {
        return new LoggingNotifier(this);
    }

    @Override
    public String getType() {
        return "logging";
    }
}
