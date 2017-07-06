package be.fgov.caamihziv.services.quickmon.domain.notifiers;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by gs on 08.06.17.
 */
public interface NotifiersRepository {

    Collection<Notifier> findAll(Predicate<Notifier> predicate);

    Notifier findByName(String name);

    void save(Set<Notifier> notifiers);

    void deleteAll();
}
