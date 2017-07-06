package be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckBuilder;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckRepository;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;
import be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks.lucene.QueryNode;
import be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks.lucene.QueryNodeEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by gs on 15.04.17.
 */
@RestController
@RequestMapping(value = "/api/healthChecks", produces = MediaTypes.HAL_JSON_VALUE)
public class HealthChecksController {

    private final HealthCheckRepository healthCheckRepository;

    @Autowired
    public HealthChecksController(HealthCheckRepository healthCheckRepository) {
        this.healthCheckRepository = healthCheckRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(QueryNode.class, new QueryNodeEditor());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResourceSupport root() {
        ResourceSupport root = new ResourceSupport();
        root.add(linkTo(methodOn(HealthChecksController.class).search(new HealthChecksQuery())).withRel("search"));
        root.add(linkTo(methodOn(HealthChecksController.class).searchTags(new HealthChecksQuery())).withRel("searchTags"));
        root.add(linkTo(methodOn(HealthChecksController.class).searchStatuses(new HealthChecksQuery())).withRel("searchStatuses"));
        root.add(new Link(linkTo(methodOn(HealthChecksController.class).getOne("")).toString() + "{name}", "healthCheck"));
        return root;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public FullHealthCheck getOne(@PathVariable("name") String name) {
        return convertToFullResource(healthCheckRepository.findByName(name));
    }

    private FullHealthCheck convertToFullResource(HealthCheck healthCheck) {
        FullHealthCheck healthCheckResource = new FullHealthCheck(healthCheck);
        healthCheckResource.add(linkTo(methodOn(HealthChecksController.class).getOne(healthCheck.getName())).withSelfRel());
        return healthCheckResource;
    }



    @RequestMapping(value = "/search/tags/count{?tags,status,luceneQuery}", method = RequestMethod.GET)
    public Resource<Map<String,Integer>> searchTags(HealthChecksQuery query) {
        List<LightweightHealthCheck> healthChecks = healthCheckRepository.findAll(query.asPredicate()).stream().map(hc -> new LightweightHealthCheck(hc)).collect(Collectors.toList());

        Map<String, Integer> countTags = new TreeMap<>();
        healthChecks.stream().forEach(
                healthcheck -> {
                    healthcheck.getTags().stream().forEach( tag -> {
                        if (countTags.containsKey(tag)) {
                            countTags.put(tag, countTags.get(tag) + 1);
                        } else {
                            countTags.put(tag, 1);
                        }
                    });
                }
        );

        Resource<Map<String, Integer>> countTagsResource = new Resource<>(countTags);
        countTagsResource.add(linkTo(methodOn(HealthChecksController.class).searchTags(query)).withSelfRel());
        return countTagsResource;
    }

    @RequestMapping(value = "/search/statuses/count{?tags,luceneQuery}", method = RequestMethod.GET)
    public Resource<Map<HealthStatus.Health,Integer>> searchStatuses(HealthChecksQuery query) {
        Collection<HealthCheck> healthChecks = healthCheckRepository.findAll(query.asPredicate());

        HashMap<HealthStatus.Health, Integer> countStatuses = new HashMap<>();
        healthChecks.stream().map(hc -> hc.getLastStatus()).forEach(
            healthStatus -> {
                if (countStatuses.containsKey(healthStatus.getHealth())) {
                    countStatuses.put(healthStatus.getHealth(), countStatuses.get(healthStatus.getHealth()) + 1);
                } else {
                    countStatuses.put(healthStatus.getHealth(), 1);
                }
            }
        );

        Resource<Map<HealthStatus.Health, Integer>> countTagsResource = new Resource<>(countStatuses);
        countTagsResource.add(linkTo(methodOn(HealthChecksController.class).searchStatuses(query)).withSelfRel());
        return countTagsResource;
    }

    @RequestMapping(value = "/search{?tags,status,luceneQuery}", method = RequestMethod.GET)
    public Resources search(HealthChecksQuery query) {
        List<LightweightHealthCheck> healthChecks = healthCheckRepository.findAll(query.asPredicate()).stream().map(hc -> new LightweightHealthCheck(hc)).collect(Collectors.toList());
        healthChecks.sort((hc1, hc2) -> hc1.getName().compareTo(hc2.getName()));
        Resources<Resource<LightweightHealthCheck>> resources = Resources.wrap(healthChecks);
        resources.add(linkTo(methodOn(HealthChecksController.class).search(query)).withSelfRel());
        return resources;
    }

    @RequestMapping(method = DELETE)
    public void deleteAll() {
        this.healthCheckRepository.deleteAll();
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> save(@RequestBody HealthCheckBuilder ... cmds) {
        Optional<HealthCheckBuilder> invalidBuilder = Arrays.stream(cmds).filter(builder -> !builder.isValid()).findFirst();

        if (invalidBuilder.isPresent()) {
            Map<String, Object> result = new HashMap<>();
            result.put("builder", invalidBuilder.get());
            result.put("errors", invalidBuilder.get().validate());
            return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
        }

        Set<HealthCheck> healthChecks = Arrays.stream(cmds).map(builder -> builder.build()).collect(Collectors.toSet());
        healthCheckRepository.save(healthChecks);

        HttpHeaders httpHeaders = new HttpHeaders();
        healthChecks.stream().forEach(healthCheck ->
            httpHeaders.add("Location", linkTo(HealthChecksController.class).slash(healthCheck.getName()).toString())
        );
        return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
    }

}
