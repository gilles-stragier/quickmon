package be.fgov.caamihziv.services;

import be.fgov.caamihziv.services.quickmon.domain.aggregation.mostrecent.MostRecentAggregatorBuilder;
import be.fgov.caamihziv.services.quickmon.domain.assertions.spel.SpelAssertionBuilder;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.http.HttpSamplerBuilder;
import be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks.lucene.QueryNode;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Created by gs on 25.04.17.
 */
public class SandboxMain {

    public static void main(String[] args) {
        RestTemplate rt = new RestTemplate();
    }

}
