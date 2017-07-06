package be.fgov.caamihziv.services.quickmon.domain.assertions;

import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Created by gs on 24.04.17.
 */
public abstract class AbstractAssertion<T> implements Assertion<T> {

    protected Result ok() {
        return new Result(true, isCritical(), Optional.empty());
    }

    protected Result nok(String message) {
        Assert.notNull(message, "A message is mandatory for nok results");
        return new Result(false, isCritical(), Optional.of(message));
    }

    private boolean critical;

    public AbstractAssertion(boolean critical) {
        this.critical = critical;
    }

    @Override
    public boolean isCritical() {
        return critical;
    }
}
