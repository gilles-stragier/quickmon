package be.fgov.caamihziv.services.quickmon.domain.assertions.spel;

import be.fgov.caamihziv.services.quickmon.domain.ValidationUtils;
import be.fgov.caamihziv.services.quickmon.domain.assertions.AssertionBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.http.HttpSampler;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

import static be.fgov.caamihziv.services.quickmon.domain.ValidationUtils.notNull;

/**
 * Created by gs on 24.04.17.
 */
@JsonTypeName("spel")
public class SpelAssertionBuilder extends AssertionBuilder<SpelAssertionBuilder, SpelAssertion> {

    private String expression;

    public SpelAssertionBuilder expression(String val) {
        this.expression = val;
        return this;
    }

    @Override
    public List<String> validate() {
        List<String> errors = super.validate();
        notNull(errors, expression, "Please provide a spel expression");
        return errors;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String getType() {
        return "spel";
    }

    @Override
    public SpelAssertion build() {
        return new SpelAssertion(this);
    }
}
