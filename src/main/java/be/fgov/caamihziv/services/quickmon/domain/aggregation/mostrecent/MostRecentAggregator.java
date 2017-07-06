package be.fgov.caamihziv.services.quickmon.domain.aggregation.mostrecent;

import be.fgov.caamihziv.services.quickmon.domain.aggregation.AbstractHealthAggregator;
import be.fgov.caamihziv.services.quickmon.domain.aggregation.AggregatorBuilder;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;

/**
 * Created by gs on 24.04.17.
 */
public class MostRecentAggregator extends AbstractHealthAggregator {

    public MostRecentAggregator(MostRecentAggregatorBuilder builder) {
        super(builder.getWindow());
    }

    @Override
    protected HealthStatus doComputeHealth(HealthStatus status) {
        return status;
    }

    @Override
    public AggregatorBuilder toBuilder() {
        return new MostRecentAggregatorBuilder()
                .window(getWindow());
    }

}
