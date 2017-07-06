package be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks.lucene;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * Created by gs on 17.05.17.
 */
public class HealthChecksQueryNodeInterpreter {

    public Predicate<HealthCheck> toPredicate(QueryNode queryNode) {
        if (queryNode.isEmpty()) {
            return hcs -> true;
        }

        if (queryNode.isTerm()) {
            return term(queryNode.isDefaultField(), queryNode.getPrefix(), queryNode.getField(), queryNode.getTerm());
        } else {
            switch (queryNode.getOperator()) {
                case OR:
                    if (queryNode.getLeft() != null && queryNode.getRight() != null) {
                        return toPredicate(queryNode.getLeft()).or(toPredicate(queryNode.getRight()));
                    } else if (queryNode.getLeft() != null){
                        // We basically have a single predicate with an OR operator, which is ugly, but happens
                        return toPredicate(queryNode.getLeft());
                    } else {
                        return toPredicate(queryNode.getRight());
                    }
                case AND:
                    return toPredicate(queryNode.getLeft()).and(toPredicate(queryNode.getRight()));
                case NOT:
                    if (queryNode.getLeft() != null) {
                        return toPredicate(queryNode.getLeft()).negate();
                    } else {
                        return toPredicate(queryNode.getRight()).negate();
                    }
                default:
                    throw new IllegalArgumentException("Unknown operator " + queryNode.getOperator());
            }
        }
    }

    private Predicate<HealthCheck> term(boolean defaultField, String prefix, String field, String term) {
        // Prefix
        boolean negate = false;
        if ("-".equals(prefix)) {
            negate = true;
        }

        // Default field
        if (defaultField) {
            return negate ? predicateOfField("name", term).negate() : predicateOfField("name", term);
        } else {
            return negate ? predicateOfField(field, term).negate() : predicateOfField(field, term);
        }

        //TODO - Manage multivalued terms
    }

    private Predicate<HealthCheck> predicateOfField(String field, String term) {
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(HealthCheck.class, field);
        if (propertyDescriptor == null) {
            // The field is not found, so we just do as if everything matches
            // TODO - Maybe we could log that :)
            return hc -> true;
        } else if (String.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
            if (term.contains("*")) {
                // We have a regex here
                return hc -> invoke(hc, propertyDescriptor.getReadMethod(), String.class).matches(term.replace("*", ".*"));
            } else {
                return hc -> invoke(hc, propertyDescriptor.getReadMethod(), String.class).contains(term);
            }
        } else {
            if (term.contains("*")) {
                return hc -> invoke(hc, propertyDescriptor.getReadMethod(), Object.class).toString().matches(".*" + term.replace("*", ".*") + ".*");
            } else {
                return hc -> invoke(hc, propertyDescriptor.getReadMethod(), Object.class).toString().contains(term);
            }

        }
    }

    private <T> T invoke (Object target, Method method, Class<T> returnType) {
        try {
            return (T) method.invoke(target);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Error invoking method " + method, e);
        }
    }


}
