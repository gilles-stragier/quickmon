package be.fgov.caamihziv.services.quickmon.domain.notifiers;

import be.fgov.caamihziv.services.quickmon.domain.Builder;
import be.fgov.caamihziv.services.quickmon.domain.ValidationUtils;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.logging.LoggingNotifierBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.http.HttpSamplerBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.ssh.JschSamplerBuilder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by gs on 08.06.17.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoggingNotifierBuilder.class, name = "logging")
})
public abstract class NotifierBuilder<T extends NotifierBuilder, U extends Notifier> implements Builder<T,U> {

    private String name;
    private Duration period;
    private Collection<String> tags;
    private Collection<HealthStatus.Health> statuses;
    private boolean onlyOnChange = true;

    public NotifierBuilder() {
        this.tags = new ArrayList<>();
        this.statuses = Arrays.asList(HealthStatus.Health.WARNING, HealthStatus.Health.CRITICAL);
        this.period = Duration.ofMinutes(30);
    }

    public T tags(Collection<String> val) {
        this.tags = val;
        return (T) this;
    }

    public T statuses(Collection<HealthStatus.Health> val) {
        this.statuses = val;
        return (T) this;
    }

    public T period(Duration val) {
        this.period = val;
        return (T) this;
    }

    public T name(String val) {
        this.name = val;
        return (T) this;
    }

    public T onlyOnChange(boolean val) {
        this.onlyOnChange = val;
        return (T) this;
    }


    @Override
    public List<String> validate() {
        ArrayList<String> errors = new ArrayList<>();
        ValidationUtils.notNull(errors, name, "Please provide the notifier a name");
        ValidationUtils.notNull(errors, period, "Please provide the notifier a period");
        return errors;
    }

    public String getName() {
        return name;
    }

    public Duration getPeriod() {
        return period;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public Collection<HealthStatus.Health> getStatuses() {
        return statuses;
    }

    public abstract String getType();

    public abstract U build();

    public boolean isOnlyOnChange() {
        return onlyOnChange;
    }
}
