package be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks;

import be.fgov.caamihziv.services.quickmon.TestCases;
import be.fgov.caamihziv.services.quickmon.domain.aggregation.mostrecent.MostRecentAggregatorBuilder;
import be.fgov.caamihziv.services.quickmon.domain.assertions.spel.SpelAssertionBuilder;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckBuilder;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckRepository;
import be.fgov.caamihziv.services.quickmon.domain.samplers.http.HttpSamplerBuilder;
import be.fgov.caamihziv.services.quickmon.infrastructure.healthchecks.JsonBackedHealthCheckRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.Arrays;

import static be.fgov.caamihziv.services.quickmon.TestCases.sampleCheck;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.Assert.assertFalse;


/**
 * Created by gs on 20.07.17.
 */
public class HealthChecksControllerIntegrationTest {


    private HealthCheckRepository healthCheckRepository;

    @Before
    public void setup() throws Exception {
        File tempFile = File.createTempFile("junit-healthchecks", "");
        tempFile.delete();

        healthCheckRepository = new JsonBackedHealthCheckRepositoryImpl(
                tempFile,

                new ObjectMapper()
        );
        RestAssuredMockMvc.standaloneSetup(
                new HealthChecksController(healthCheckRepository)
        );
    }

    @Test
    public void test_delete_of_a_health_check() {
        healthCheckRepository.save(
                sampleCheck()
                .build()
        );

        given()
                .log().all()
        .when()
                .delete("/api/healthChecks/someCheck")
        .then()
                .statusCode(204)
        ;

        assertFalse(healthCheckRepository.exists("someCheck"));
    }



}
