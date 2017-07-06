package be.fgov.caamihziv.services.quickmon.domain.aggregation.swaying;

import be.fgov.caamihziv.services.quickmon.domain.aggregation.AbstractHealthAggregator;
import be.fgov.caamihziv.services.quickmon.domain.aggregation.AggregatorBuilder;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;

import java.util.Map;
import java.util.Optional;

/**
 * Created by gs on 24.04.17.
 */
public class SwayingAggregator extends AbstractHealthAggregator {

    public SwayingAggregator(SwayingAggregatorBuilder builder) {
        super(builder.getWindow());
    }

    @Override
    protected HealthStatus doComputeHealth(HealthStatus status) {
        Optional<Map.Entry<HealthStatus.Health, Integer>> alwaysOnStatus = getStats().entrySet().stream().filter(e -> e.getValue() == 100).findFirst();
        if (alwaysOnStatus.isPresent()) {
            return new HealthStatus(alwaysOnStatus.get().getKey(), status.getDetail(), status.getMessage(), status.getAssertions());
        } else {
            return new HealthStatus(HealthStatus.Health.WARNING, "Healthcheck is swaying - check history");
        }
    }

    @Override
    public AggregatorBuilder toBuilder() {
        return new SwayingAggregatorBuilder()
                .window(getWindow());
    }

}
