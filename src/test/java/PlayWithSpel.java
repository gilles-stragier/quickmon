import be.fgov.caamihziv.services.quickmon.domain.assertions.spel.SpelExtensions;
import com.jayway.jsonpath.JsonPath;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by gs on 24.04.17.
 */
public class PlayWithSpel {

    public static class TestHelper {
        public static Object body(String input, String path) {
            JsonPath jsonPath = JsonPath.compile(path);
            return jsonPath.read(input);
        }
    }

    public static Object evaluate(EvaluationContext ctx, String expression) {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expression);
        Object value = exp.getValue(ctx);
        return value;
    }

    public static void main(String[] args) throws Exception {
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.getForEntity("http://aft.users-api.test.paas/health", String.class);

        StandardEvaluationContext ctx = new StandardEvaluationContext(response);
        ctx.registerFunction("extractFromJson", SpelExtensions.class.getMethod("extractFromJson", String.class, String.class));

        System.out.println(evaluate(ctx, "#root.statusCodeValue == 200"));
        System.out.println(evaluate(ctx, "#extractFromJson(#root.body, 'status') == 'UP'"));
        System.out.println(evaluate(ctx, "#root.headers['X-RID'] != null"));




//        System.out.println(read);
    }

}
