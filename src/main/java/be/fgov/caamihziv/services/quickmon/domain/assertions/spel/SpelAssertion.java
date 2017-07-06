package be.fgov.caamihziv.services.quickmon.domain.assertions.spel;

import be.fgov.caamihziv.services.quickmon.domain.assertions.AbstractAssertion;
import be.fgov.caamihziv.services.quickmon.domain.assertions.Assertion;
import be.fgov.caamihziv.services.quickmon.domain.assertions.AssertionBuilder;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static java.util.Arrays.stream;

/**
 * Created by gs on 24.04.17.
 */
public class SpelAssertion extends AbstractAssertion<Object> implements Assertion<Object> {

    private SpelExpressionParser parser;
    private String expression;

    public SpelAssertion(SpelAssertionBuilder builder) {
        super(builder.isCritical());
        this.parser = new SpelExpressionParser();
        this.expression = builder.getExpression();
    }

    public EvaluationContext create(Object target) {
        StandardEvaluationContext ctx = new StandardEvaluationContext(target);

        stream(SpelExtensions.class.getMethods())
                .filter(method -> method.getModifiers() == 9)
                .forEach(method -> {
                    ctx.registerFunction(method.getName(), method);
                });

        return ctx;

    }

    @Override
    public Result execute(Object target) {
        try {
            EvaluationContext ctx = create(target);
            Expression exp = parser.parseExpression(expression);
            Boolean value = (Boolean) exp.getValue(ctx);
            if (value) {
                return ok();
            } else {
                return nok("Expression " + expression + " returned false");
            }
        } catch (Exception e) {
            // TODO - Log me
            return nok("Exception : " + e.getMessage());
        }
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public AssertionBuilder toBbuilder() {
        return new SpelAssertionBuilder()
                .expression(expression)
                .critical(isCritical());
    }

    @Override
    public String toString() {
        return "spel:" + expression;
    }
}
