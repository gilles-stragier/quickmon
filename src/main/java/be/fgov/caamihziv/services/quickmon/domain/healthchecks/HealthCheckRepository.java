package be.fgov.caamihziv.services.quickmon.domain.healthchecks;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by gs on 15.04.17.
 */
public interface HealthCheckRepository {

    Collection<HealthCheck> findAll();

    Collection<HealthCheck> findAll(Predicate<HealthCheck> predicate);

    HealthCheck findByName(String name);

    void save(Set<HealthCheck> healthChecks);

    void deleteAll();

    boolean exists(String name);

    void delete(String name);

    void save(HealthCheck healthCheck);
}
