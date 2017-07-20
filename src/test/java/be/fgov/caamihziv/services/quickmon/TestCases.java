package be.fgov.caamihziv.services.quickmon;

import be.fgov.caamihziv.services.quickmon.domain.aggregation.mostrecent.MostRecentAggregatorBuilder;
import be.fgov.caamihziv.services.quickmon.domain.assertions.spel.SpelAssertionBuilder;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.http.HttpSamplerBuilder;

import java.util.Arrays;

/**
 * Created by gs on 20.07.17.
 */
public class TestCases {

    public static HealthCheckBuilder sampleCheck() {
        return new HealthCheckBuilder()
                .aggregatorBuilder(new MostRecentAggregatorBuilder())
                .assertions(Arrays.asList(
                        new SpelAssertionBuilder()
                                .expression("")
                ))
                .samplerBuilder(
                        new HttpSamplerBuilder()
                                .url("http://localhost")
                )
                .name("someCheck");
    }
}
