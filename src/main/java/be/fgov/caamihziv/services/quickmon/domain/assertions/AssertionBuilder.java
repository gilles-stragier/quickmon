package be.fgov.caamihziv.services.quickmon.domain.assertions;

import be.fgov.caamihziv.services.quickmon.domain.Builder;
import be.fgov.caamihziv.services.quickmon.domain.ValidationUtils;
import be.fgov.caamihziv.services.quickmon.domain.assertions.spel.SpelAssertionBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.Sampler;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gs on 24.04.17.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SpelAssertionBuilder.class, name = "spel")
})
public abstract class AssertionBuilder<T extends AssertionBuilder, U extends Assertion> implements Builder<T,U> {

    protected boolean critical;
    protected String message;

    public AssertionBuilder() {
        critical = true;
    }

    public T critical(boolean critical) {
        this.critical = critical;
        return (T) this;
    }

    public T message(String message) {
        this.message = message;
        return (T) this;
    }

    @Override
    public List<String> validate() {
        ArrayList<String> errors = new ArrayList<>();
        return errors;
    }

    public boolean isCritical() {
        return critical;
    }

    public String getMessage() {
        return message;
    }

    public abstract  String getType();

    public abstract U build();
}
