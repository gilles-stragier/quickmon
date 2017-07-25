package be.fgov.caamihziv.services.quickmon.domain.notifiers;

import be.fgov.caamihziv.services.quickmon.domain.Builder;
import be.fgov.caamihziv.services.quickmon.domain.ValidationUtils;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.logging.LoggingNotifierBuilder;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.mail.MailNotifierBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.http.HttpSamplerBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.ssh.JschSamplerBuilder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.scheduling.support.CronSequenceGenerator;

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
        @JsonSubTypes.Type(value = LoggingNotifierBuilder.class, name = "logging"),
        @JsonSubTypes.Type(value = MailNotifierBuilder.class, name = "mail"),
})
public abstract class NotifierBuilder<T extends NotifierBuilder, U extends Notifier> implements Builder<T,U> {

    private String name;
    private Duration period;
    private String schedulingCronExpression;

    private Collection<String> tags;
    private Collection<HealthStatus.Health> statuses;
    private boolean onlyOnChange = true;

    private LocalDateTime createdOn;

    public NotifierBuilder() {
        this.tags = new ArrayList<>();
        this.statuses = Arrays.asList(HealthStatus.Health.WARNING, HealthStatus.Health.CRITICAL);
        this.createdOn = LocalDateTime.now();
    }

    public T createdOn(LocalDateTime val) {
        this.createdOn = val;
        return (T) this;
    }

    public T schedulingCronExpression(String val) {
        this.schedulingCronExpression = val;
        return (T) this;
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
        ValidationUtils.isTrue(errors, (period != null) || (schedulingCronExpression != null), "Please provide the notifier a period or a schedulingCronExpression");
        ValidationUtils.isTrue(errors, schedulingCronExpression == null || CronSequenceGenerator.isValidExpression(schedulingCronExpression), "Invalid cron expressions : " + schedulingCronExpression);
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

    public String getSchedulingCronExpression() {
        return schedulingCronExpression;
    }

    public abstract String getType();

    public abstract U build();

    public boolean isOnlyOnChange() {
        return onlyOnChange;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
}
