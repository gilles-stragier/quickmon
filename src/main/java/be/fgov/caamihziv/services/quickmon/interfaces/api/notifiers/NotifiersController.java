package be.fgov.caamihziv.services.quickmon.interfaces.api.notifiers;

import be.fgov.caamihziv.services.quickmon.domain.notifiers.Notifier;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.NotifierBuilder;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.NotifiersRepository;
import org.springframework.hateoas.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(value = "/api/notifiers", produces = MediaTypes.HAL_JSON_VALUE)
public class NotifiersController {

    private final NotifiersRepository notifiersRepository;

    public NotifiersController(NotifiersRepository healthCheckRepository) {
        this.notifiersRepository = healthCheckRepository;
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResourceSupport root() {
        ResourceSupport root = new ResourceSupport();
        root.add(linkTo(methodOn(NotifiersController.class).search()).withRel("search"));
        root.add(new Link(linkTo(methodOn(NotifiersController.class).getOne("")).toString() + "{name}", "notifier"));
        return root;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public Resource<Notifier> getOne(@PathVariable("name") String name) {
        return new Resource<>(notifiersRepository.findByName(name));
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Resources search() {
        List notifiers = notifiersRepository.findAll(p -> true).stream().map(notifier -> new Resource(notifier)).collect(Collectors.toList());
        Resources resources = Resources.wrap(notifiers);
        resources.add(linkTo(methodOn(NotifiersController.class).search()).withSelfRel());
        return resources;
    }

    @RequestMapping(method = DELETE)
    public void deleteAll() {
        this.notifiersRepository.deleteAll();
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> save(@RequestBody NotifierBuilder ... cmds) {
        Optional<NotifierBuilder> invalidBuilder = Arrays.stream(cmds).filter(builder -> !builder.isValid()).findFirst();

        if (invalidBuilder.isPresent()) {
            Map<String, Object> result = new HashMap<>();
            result.put("builder", invalidBuilder.get());
            result.put("errors", invalidBuilder.get().validate());
            return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
        }

        Set<Notifier> notifiers = Arrays.stream(cmds).map(builder -> builder.build()).collect(Collectors.toSet());
        notifiersRepository.save(notifiers);

        HttpHeaders httpHeaders = new HttpHeaders();
        notifiers.stream().forEach(notifier ->
            httpHeaders.add("Location", linkTo(NotifiersController.class).slash(notifier.getName()).toString())
        );
        return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
    }

}
