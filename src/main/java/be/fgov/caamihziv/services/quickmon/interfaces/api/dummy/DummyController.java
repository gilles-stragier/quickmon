package be.fgov.caamihziv.services.quickmon.interfaces.api.dummy;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by gs on 24.04.17.
 */
@RestController
@RequestMapping("/api/dummyChecks")
@Profile("dummiesOn")
public class DummyController {


    public static enum Mode {
        ALWAYS_OK,
        DANGLING,
        ALWAYS_WARNING,
        ALWAYS_CRITICAL
    }

    private HttpStatus computeStatus(Mode mode) {
        switch(mode) {
            case ALWAYS_OK:
                return HttpStatus.OK;
            case ALWAYS_WARNING:
                return HttpStatus.FOUND;
            case ALWAYS_CRITICAL:
                return HttpStatus.SERVICE_UNAVAILABLE;
            case DANGLING:
                Random random = new Random();
                int randomPos = random.nextInt() % Mode.values().length;
                if (randomPos < 0) {
                    randomPos = randomPos * -1;
                }
                return computeStatus(Mode.values()[randomPos]);
            default:
                throw new IllegalArgumentException("Unable to manage mode " + mode);
        }

    }

    @RequestMapping(method = GET)
    public ResponseEntity<Void> dummyCheck(@RequestParam(name = "mode", defaultValue = "ALWAYS_OK") Mode mode) {
        return new ResponseEntity<>(computeStatus(mode));
    }

}
