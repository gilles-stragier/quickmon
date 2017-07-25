package be.fgov.caamihziv.services.quickmon.domain.notifiers;

import be.fgov.caamihziv.services.quickmon.TestCases;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckRepository;
import be.fgov.caamihziv.services.quickmon.infrastructure.healthchecks.JsonBackedHealthCheckRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by gs on 24.07.17.
 */
public class AbstractNotifierTest {

    private HealthCheckRepository healthCheckRepository;

    @Before
    public void setup() throws Exception {
        File tempFile = File.createTempFile("junit-healthchecks", "");
        tempFile.delete();

        healthCheckRepository = new JsonBackedHealthCheckRepositoryImpl(
                tempFile,
                new ObjectMapper()
        );
    }

    public class DummyNotifierBuilder extends NotifierBuilder<DummyNotifierBuilder, DummyNotifier> {

        @Override
        public String getType() {
            return "";
        }

        @Override
        public DummyNotifier build() {
            return new DummyNotifier(this);
        }

        @Override
        public boolean isValid() {
            return false;
        }
    }
    public class DummyNotifier extends AbstractNotifier {

        private boolean doRun = false;

        public DummyNotifier(DummyNotifierBuilder builder) {
            super(builder);
        }

        @Override
        public <R extends NotifierBuilder> R toBuilder() {
            return null;
        }

        public void resetDoRun() {
            doRun = false;
        }

        @Override
        public void doRun(Collection<HealthCheck> healthChecks) {
            doRun = true;
        }

        public boolean isDoRun() {
            return doRun;
        }

        public void setLastTimeRun(LocalDateTime lastTimeRun) {
            Field lastTimeRunField = ReflectionUtils.findField(AbstractNotifier.class, "lastTimeRun");
            ReflectionUtils.makeAccessible(lastTimeRunField);
            ReflectionUtils.setField(lastTimeRunField, this, lastTimeRun);
        }
    }

    @Test
    public void test_notifier_on_change() {
        HealthCheck sampleCheck = TestCases.sampleCheck().build();

        healthCheckRepository.save(sampleCheck);

        DummyNotifier dummy = new DummyNotifierBuilder()
                .build();
        dummy.run(healthCheckRepository);
        dummy.resetDoRun();
        dummy.run(healthCheckRepository);

        assertFalse(dummy.isDoRun());

    }

    @Test
    public void test_invalid_cron_expression() {
        DummyNotifierBuilder builder = new DummyNotifierBuilder()
                .name("tutu")
                .schedulingCronExpression("tutut");

        assertFalse(builder.isValid());
        assertTrue(builder.validate().contains("Invalid cron expressions : tutut"));

    }

    @Test
    public void test_valid_cron_expression_should_run() {
        DummyNotifier notifier = new DummyNotifierBuilder()
                .schedulingCronExpression("0 0 * * * *")
                .build();

        LocalDateTime hardCodedTime = LocalDateTime.of(2000, 1, 1, 1, 30);
        notifier.setLastTimeRun(hardCodedTime);
        assertTrue(notifier.shouldRun());
    }

    @Test
    public void test_valid_cron_expression_should_not_run() {
        DummyNotifier notifier = new DummyNotifierBuilder()
                .schedulingCronExpression("0 0 * * * *")
                .build();

        LocalDateTime hardCodedTime = LocalDateTime.of(2999, 1, 1, 1, 30);
        notifier.setLastTimeRun(hardCodedTime);
        assertFalse(notifier.shouldRun());
    }


}