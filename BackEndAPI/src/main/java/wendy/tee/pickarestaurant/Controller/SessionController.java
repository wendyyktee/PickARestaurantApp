package wendy.tee.pickarestaurant.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wendy.tee.pickarestaurant.Enum.SessionStatus;
import wendy.tee.pickarestaurant.Model.Restaurant;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Service.RestaurantService;
import wendy.tee.pickarestaurant.Service.ResultService;
import wendy.tee.pickarestaurant.Service.SessionService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/session")
@SessionAttributes("sessionKey")
public class SessionController {

    @Autowired
    SessionService sessionService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    ResultService resultService;

    private Logger logger = LoggerFactory.getLogger(
            SessionController.class);

    @GetMapping("/initiateSession")
    public ResponseEntity<?> initiateSession(HttpSession httpSession, HttpServletResponse httpResponse) {
        Cookie cookie = new Cookie("persist-session-id", httpSession.getId());
        cookie.setMaxAge(60*60*12);
        cookie.setSecure(false);
        cookie.setHttpOnly(false);
        httpResponse.addCookie(cookie);

        Session session = sessionService.createNewSession(httpSession.getId());

        if (session != null) {
            if (session.getSessionCode() != null) {
                return new ResponseEntity<>(sessionService.convertSessionToSessionResponse(session), HttpStatus.OK);
            } else {
                logger.error("Session initialize failed - Session created without sessionCode");
                logger.error(httpSession.getId());
                logger.error(String.valueOf(httpSession.getCreationTime()));

                return new ResponseEntity<>("Session initialize error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.error("Session initialize failed - No session created");
            logger.error(httpSession.getId());
            logger.error(String.valueOf(httpSession.getCreationTime()));

            return new ResponseEntity<>("Session initialize failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    When user join the session with the url provided, there will be 3 possibility
    1. Session code doesn't exist in the system, throw http NOT_FOUND error code
    2. Session code exist, session_status = CLOSED, end_time > 1 hour - considered as invalid session code,
    throw http NOT_FOUND error code
    3. Session code exist, session_status = ACTIVE or (session_status = CLOSED & end_time < 1 hour)
    - will return session details
     */
    @GetMapping("/{sessionCode}")
    public ResponseEntity<?> validateSession(HttpSession httpSession, @PathVariable String sessionCode, HttpServletRequest request) {
        List<Session> optionalSessionList = sessionService.findActiveSessionBySessionCode(sessionCode);
        System.out.println(httpSession.getId());
        System.out.println(request.getCookies());

        logger.info(String.valueOf(optionalSessionList.size()));
        if (!optionalSessionList.isEmpty()) {
            Session session = optionalSessionList.get(0);
            logger.info(String.valueOf(session));
            logger.info(session.getStatus().toString());

            if (session.getStatus().equals(SessionStatus.ACTIVE)
                || (session.getStatus().equals(SessionStatus.CLOSED)
                    && session.getEndTime().isAfter(LocalDateTime.now().minusHours(1)))) {

                return new ResponseEntity<>(sessionService.convertSessionToSessionResponse(session), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/endSession/{sessionCode}")
    public ResponseEntity<?> endSession(@PathVariable String sessionCode, HttpSession httpSession) {

        List<Session> optionalSessionList = sessionService.findActiveSessionBySessionCode(sessionCode);

        if (!optionalSessionList.isEmpty()) {
            Session session = optionalSessionList.get(0);

            if (session.getInitiatorHttpSessionId().equalsIgnoreCase(httpSession.getId())) {
                if (session.getStatus().equals(SessionStatus.ACTIVE)) {
                    Restaurant restaurant = restaurantService.randomPickARestaurantBySessionId(session.getId());
                    if (restaurant != null) {
                        sessionService.endSession(session);
                        return new ResponseEntity<>(restaurant.getRestaurantName(), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
