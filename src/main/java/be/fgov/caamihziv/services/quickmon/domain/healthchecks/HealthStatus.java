package be.fgov.caamihziv.services.quickmon.domain.healthchecks;

import be.fgov.caamihziv.services.quickmon.domain.assertions.Assertion;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by gs on 15.04.17.
 */
public class HealthStatus<T> {



    public static enum Health {
        UNCHECKED,
        WARNING,
        CRITICAL,
        OK,
        UNKNOWN
    }

    private Map<Assertion, Assertion.Result> assertions;
    private Health health;
    private String message;
    private T detail;
    private LocalDateTime checkedOn;

    public HealthStatus(Health health, String message) {
        this(health, null, message, null);
    }

    public HealthStatus(Health health, T detail, String message) {
        this(health, detail, message,null);
    }

    public HealthStatus(Health health, T detail, String message, Map<Assertion, Assertion.Result> assertions) {
        this.health = health;
        this.detail = detail;
        this.checkedOn = LocalDateTime.now();
        this.assertions = assertions;
        this.message = message;
    }

    public Health getHealth() {
        return health;
    }

    public T getDetail() {
        return detail;
    }

    public LocalDateTime getCheckedOn() {
        return checkedOn;
    }

    public Map<Assertion, Assertion.Result> getAssertions() {
        return assertions;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "HealthStatus{" +
                "assertions=" + assertions +
                ", health=" + health +
                ", detail=" + detail +
                ", checkedOn=" + checkedOn +
                '}';
    }
}
