package be.fgov.caamihziv.services.quickmon.domain.aggregation.swaying;

import be.fgov.caamihziv.services.quickmon.domain.aggregation.AggregatorBuilder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by gs on 24.04.17.
 */
@JsonTypeName("swaying")
public class SwayingAggregatorBuilder extends AggregatorBuilder<SwayingAggregatorBuilder, SwayingAggregator> {


    @Override
    public String getType() {
        return "swaying";
    }


    @Override
    public SwayingAggregator build() {
        return new SwayingAggregator(this);

    }
}
