package be.fgov.caamihziv.services.quickmon.interfaces.api;

import be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks.HealthChecksController;
import be.fgov.caamihziv.services.quickmon.interfaces.api.notifiers.NotifiersController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gs on 16.04.17.
 */
@RestController
@RequestMapping("/api")
public class RootController {

    @RequestMapping(method = RequestMethod.GET)
    public ResourceSupport root() {
        ResourceSupport root = new ResourceSupport();
        root.add(ControllerLinkBuilder.linkTo(HealthChecksController.class).withRel("healthChecks"));
        root.add(ControllerLinkBuilder.linkTo(NotifiersController.class).withRel("notifiers"));
        return root;
    }

}
