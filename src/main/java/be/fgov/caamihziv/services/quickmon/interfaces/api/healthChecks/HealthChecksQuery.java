package be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;
import be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks.lucene.HealthChecksQueryNodeInterpreter;
import be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks.lucene.QueryNode;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by gs on 26.04.17.
 */
public class HealthChecksQuery {

    private List<String> tags;
    private HealthStatus.Health status;
    private QueryNode luceneQuery;

    public Predicate<HealthCheck> asPredicate() {
        Predicate<HealthCheck> predicate = h -> true;
        if (tags != null && !tags.isEmpty()) {
            predicate = predicate.and(h -> h.getTags().containsAll(tags));
        }
        if (status != null) {
            predicate = predicate.and(h -> h.getLastStatus().getHealth() == status);
        }
        if (luceneQuery != null) {
            HealthChecksQueryNodeInterpreter interpreter = new HealthChecksQueryNodeInterpreter();
            Predicate<HealthCheck> luceneQueryPredicate = interpreter.toPredicate(getLuceneQuery());
            predicate = predicate.and(luceneQueryPredicate);
        }
        return predicate;
    }

    public HealthStatus.Health getStatus() {
        return status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setStatus(HealthStatus.Health status) {
        this.status = status;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public QueryNode getLuceneQuery() {
        return luceneQuery;
    }

    public void setLuceneQuery(QueryNode luceneQuery) {
        this.luceneQuery = luceneQuery;
    }
}