package be.fgov.caamihziv.services.quickmon.domain.aggregation;

import be.fgov.caamihziv.services.quickmon.domain.Builder;
import be.fgov.caamihziv.services.quickmon.domain.ValidationUtils;
import be.fgov.caamihziv.services.quickmon.domain.aggregation.mostrecent.MostRecentAggregatorBuilder;
import be.fgov.caamihziv.services.quickmon.domain.aggregation.swaying.SwayingAggregatorBuilder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gs on 24.04.17.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MostRecentAggregatorBuilder.class, name = "mostRecent"),
        @JsonSubTypes.Type(value = SwayingAggregatorBuilder.class, name = "swaying")
})
public abstract class AggregatorBuilder<T extends AggregatorBuilder, U extends HealthAggregator> implements Builder<T,U> {

    protected Duration window;

    public AggregatorBuilder() {
        this.window = Duration.ofMinutes(10);
    }

    public T window(Duration val) {
        this.window = val;
        return (T) this;
    }

    public Duration getWindow() {
        return window;
    }

    @Override
    public List<String> validate() {
        ArrayList<String> errors = new ArrayList<>();
        ValidationUtils.notNull(errors, window, "Aggregation is always computed in a time window. Please provide a duration value for the window");
        return errors;
    }

    public abstract  String getType();

    public abstract U build();
}
