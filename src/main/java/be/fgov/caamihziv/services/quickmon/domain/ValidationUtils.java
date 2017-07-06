package be.fgov.caamihziv.services.quickmon.domain;

import java.util.Collection;

/**
 * Created by gs on 26.04.17.
 */
public class ValidationUtils {

    public static void notNull(Collection<String> errors, Object target, String message) {
        if (target == null) {
            errors.add(message);
        }
    }

    public static void notEmpty(Collection<String> errors, Collection target, String message) {
        if (target == null || target.isEmpty()) {
            errors.add(message);
        }
    }

}
