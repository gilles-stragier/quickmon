package be.fgov.caamihziv.services.quickmon.domain.aggregation.mostrecent;

import be.fgov.caamihziv.services.quickmon.domain.aggregation.AggregatorBuilder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by gs on 24.04.17.
 */
@JsonTypeName("mostRecent")
public class MostRecentAggregatorBuilder extends AggregatorBuilder<MostRecentAggregatorBuilder, MostRecentAggregator> {


    @Override
    public String getType() {
        return "mostRecent";
    }


    @Override
    public MostRecentAggregator build() {
        return new MostRecentAggregator(this);

    }
}
