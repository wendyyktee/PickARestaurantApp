package wendy.tee.pickarestaurant.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wendy.tee.pickarestaurant.Model.Restaurant;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Service.RestaurantService;
import wendy.tee.pickarestaurant.Service.SessionService;
import wendy.tee.pickarestaurant.Enum.SessionStatus;

import java.util.List;

@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    SessionService sessionService;

    @Autowired
    RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<?> createNewSession() {

        Session session = sessionService.createNewSession();

        if (session != null && session.getSessionCode() != null) {
            return new ResponseEntity<>(session.getSessionCode(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{sessionCode}")
    public ResponseEntity<?> joinSession(@PathVariable String sessionCode) {

        List<Session> optionalSessionList = sessionService.findActiveSessionBySessionCode(sessionCode);

        if (!optionalSessionList.isEmpty()) {
            Session session = optionalSessionList.get(0);
            if (session.getStatus().equals(SessionStatus.ACTIVE)) {
                List<Restaurant> restaurantList = restaurantService.findBySessionId(session.getId());
                return new ResponseEntity<>(restaurantList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{sessionCode}")
    public ResponseEntity<?> endSession(@PathVariable String sessionCode) {

        List<Session> optionalSessionList = sessionService.findActiveSessionBySessionCode(sessionCode);

        if (!optionalSessionList.isEmpty()) {
            Session session = optionalSessionList.get(0);

            if (session.getStatus().equals(SessionStatus.ACTIVE)) {
                sessionService.endSession(session);
                //todo - to generate result
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
