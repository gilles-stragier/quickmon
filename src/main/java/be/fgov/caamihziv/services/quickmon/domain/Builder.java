package be.fgov.caamihziv.services.quickmon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by gs on 26.04.17.
 */
public interface Builder<T extends Builder, U> {

    List<String> validate();

    @JsonIgnore
    default boolean isValid() {
        List<String> errors = validate();
        return errors == null || errors.isEmpty();
    }

}
