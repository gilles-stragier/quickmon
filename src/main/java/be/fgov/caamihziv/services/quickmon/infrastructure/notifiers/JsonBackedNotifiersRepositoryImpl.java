package be.fgov.caamihziv.services.quickmon.infrastructure.notifiers;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckBuilder;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.Notifier;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.NotifierBuilder;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.NotifiersRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by gs on 15.04.17.
 */
@Component
public class JsonBackedNotifiersRepositoryImpl implements NotifiersRepository {

    private Set<Notifier> notifierSet;
    private File notifierFile;
    private ObjectMapper objectMapper;

    @Autowired
    public JsonBackedNotifiersRepositoryImpl(@Value("${notifiers.file}") File notifierFile, ObjectMapper objectMapper) throws IOException {
        this.notifierSet = ConcurrentHashMap.newKeySet();
        this.notifierFile = notifierFile;
        this.objectMapper = objectMapper;


        if (notifierFile.exists()) {
            loadFromFile();
        } else {
            notifierFile.createNewFile();
            writeToFile();
        }
    }

    protected void loadFromFile() throws IOException {
        Collection<? extends NotifierBuilder> collection = objectMapper.readValue(
                new FileReader(notifierFile),
                new TypeReference<Set<NotifierBuilder>>() {
                }
        );
        this.notifierSet.addAll(collection.stream().map(builder -> builder.build()).collect(Collectors.toSet()));
    }

    protected void writeToFile() {
        try {
            objectMapper.writeValue(new FileWriter(notifierFile), notifierSet.stream().map(notifier -> notifier.toBuilder()).collect(Collectors.toSet()));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write Json file", e);
        }
    }

    @Override
    public Collection<Notifier> findAll(Predicate<Notifier> predicate) {
        return Collections.unmodifiableSet(notifierSet.stream().filter(predicate).collect(Collectors.toSet()));
    }


    @Override
    public synchronized void save(Set<Notifier> notifiers) {
        notifierSet.removeAll(notifiers);
        notifierSet.addAll(notifiers);
        writeToFile();
    }

    @Override
    public void deleteAll() {
        notifierSet.clear();
        writeToFile();
    }

    @Override
    public Notifier findByName(String name) {
        return notifierSet.stream().filter(noti -> noti.getName().equals(name)).findFirst().orElse(null);
    }
}
