package be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks.lucene;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by gs on 12-12-16.
 */
public class QueryNode {

    public static enum OP {
        AND, NOT, OR
    }

    private String field;
    private String term;
    private QueryNode left;
    private QueryNode right;
    private String operator;
    private String prefix;

    public QueryNode getLeft() {
        return left;
    }

    public QueryNode getRight() {
        return right;
    }

    public String getTerm() {
        return term;
    }

    public OP getOperator() {
        return Arrays.stream(OP.values()).filter(op -> op.name().equals(operator)).findFirst().orElse(OP.OR);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getField() {
        return field;
    }

    public boolean isTerm(){
        return term != null;
    }

    public boolean isDefaultField() {
        return isTerm() && "<implicit>".equals(field);
    }

    public boolean isOperator() {
        return operator != null;
    }

    public boolean isEmpty() {
        return term == null && left == null && right == null && field == null && operator == null;
    }

}
