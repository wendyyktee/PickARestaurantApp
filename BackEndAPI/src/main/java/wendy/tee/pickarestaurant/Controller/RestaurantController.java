package wendy.tee.pickarestaurant.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wendy.tee.pickarestaurant.Enum.SessionStatus;
import wendy.tee.pickarestaurant.Model.Restaurant;
import wendy.tee.pickarestaurant.Model.Result;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Service.RestaurantService;
import wendy.tee.pickarestaurant.Service.SessionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("restaurant")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    SessionService sessionService;


    @GetMapping
    public ResponseEntity<?> getRestaurantBySessionCode(@RequestParam Long sessionId, HttpSession httpSession, HttpServletRequest request) {
        System.out.println(httpSession.getId());
        System.out.println(request.getCookies());
        List<Restaurant> restaurantList = restaurantService.findBySessionId(sessionId);
        return new ResponseEntity<>(restaurantList, HttpStatus.OK);
    }

    @PutMapping("/addRestaurant")
    public ResponseEntity<?> addRestaurant(@RequestBody Restaurant restaurant) {
        Optional<Session> optionalSession = sessionService.findById(restaurant.getSessionId());

        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();

            if (session.getStatus().equals(SessionStatus.ACTIVE)) {
                restaurantService.addRestaurant(restaurant);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
