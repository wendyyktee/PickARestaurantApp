package wendy.tee.pickarestaurant.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wendy.tee.pickarestaurant.Enum.SessionStatus;
import wendy.tee.pickarestaurant.Model.Restaurant;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Service.RestaurantService;
import wendy.tee.pickarestaurant.Service.SessionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("restaurant")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    SessionService sessionService;

    /**
     * @param sessionId - unique id for Session object
     * @return list of Restaurant & HttpStatus.OK if validation passed
     * @return - HttpStatus.BAD_REQUEST if the session provided is not exists in database or already ended
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRestaurantBySessionCode(@RequestParam String sessionId) {
        Optional<Session> optionalSession = sessionService.findById(sessionId);

        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            if (session.getStatus().equals(SessionStatus.ACTIVE)) {
                List<Restaurant> restaurantList = restaurantService.findBySessionId(sessionId);
                return new ResponseEntity<>(restaurantList, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * @param restaurant
     * @return - HttpStatus.OK if validation passed
     * @return - HttpStatus.BAD_REQUEST if the session provided is not exists in database or already ended
     */
    @PutMapping(value = "/addRestaurant", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addRestaurant(@RequestBody Restaurant restaurant) {
        Optional<Session> optionalSession = sessionService.findById(restaurant.getSessionId());

        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();

            if (session.getStatus().equals(SessionStatus.ACTIVE)) {
                restaurantService.addRestaurant(restaurant);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
