package be.fgov.caamihziv.services.quickmon.infrastructure.healthchecks;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckBuilder;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.util.Assert.isTrue;

/**
 * Created by gs on 15.04.17.
 */
@Component
public class JsonBackedHealthCheckRepositoryImpl implements HealthCheckRepository {

    private Set<HealthCheck> healthCheckList;
    private File healthChecksFile;
    private ObjectMapper objectMapper;

    @Autowired
    public JsonBackedHealthCheckRepositoryImpl(@Value("${healthchecks.file}") File healthChecksFile, ObjectMapper objectMapper) throws IOException {
        this.healthCheckList = ConcurrentHashMap.newKeySet();
        this.healthChecksFile = healthChecksFile;
        this.objectMapper = objectMapper;


        if (healthChecksFile.exists()) {
            loadFromFile();
        } else {
            healthChecksFile.createNewFile();
            writeToFile();
        }
    }

    protected void loadFromFile() throws IOException {
        Collection<? extends HealthCheckBuilder> collection = objectMapper.readValue(
                new FileReader(healthChecksFile),
                new TypeReference<Set<HealthCheckBuilder>>() {
                }
        );
        this.healthCheckList.addAll(collection.stream().map(builder -> builder.build()).collect(Collectors.toSet()));
    }

    protected void writeToFile() {
        try {
            objectMapper.writeValue(new FileWriter(healthChecksFile), healthCheckList.stream().map(hc -> hc.toBuilder()).collect(Collectors.toSet()));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write Json file", e);
        }
    }

    @Override
    public Collection<HealthCheck> findAll() {
        return Collections.unmodifiableSet(healthCheckList);
    }

    @Override
    public Collection<HealthCheck> findAll(Predicate<HealthCheck> predicate) {
        return Collections.unmodifiableSet(healthCheckList.stream().filter(predicate).collect(Collectors.toSet()));
    }

    @Override
    public HealthCheck findByName(String name) {
        return healthCheckList.stream().filter(hc -> hc.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public synchronized void save(Set<HealthCheck> healthChecks) {
        healthCheckList.removeAll(healthChecks);
        healthCheckList.addAll(healthChecks);
        writeToFile();
    }

    public synchronized void save(HealthCheck healthCheck) {
        healthCheckList.remove(healthCheck);
        healthCheckList.add(healthCheck);
        writeToFile();
    }

    @Override
    public void deleteAll() {
        healthCheckList.clear();
        writeToFile();
    }

    @Override
    public boolean exists(String name) {
        return findByName(name) != null;
    }

    @Override
    public void delete(String name) {
        HealthCheck healthCheck = findByName(name);
        if (healthCheck != null) {
            healthCheckList.remove(healthCheck);
            writeToFile();
        }
    }
}
