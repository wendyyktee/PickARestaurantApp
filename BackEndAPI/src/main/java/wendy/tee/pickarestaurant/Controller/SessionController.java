package wendy.tee.pickarestaurant.Controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wendy.tee.pickarestaurant.Enum.SessionStatus;
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

    /**
     * To start a new session, front-end application will send userSessionId to persist the session, which will be used
     * to verification when end session     *
     *
     * @param httpSession - get id from http session for identify initiator use
     * @return Session object if session initiate successfully
     * @throws org.springframework.web.client.HttpServerErrorException.InternalServerError if session initiate failed
     */
    @GetMapping("/initiateSession")
    public ResponseEntity<?> initiateSession(HttpSession httpSession) {
        Session session = sessionService.createNewSession(httpSession.getId());

        if (session != null) {
            if (session.getSessionCode() != null) {
                return new ResponseEntity<>(session, HttpStatus.OK);
            }
            else {
                logger.error("Session initialize failed - Session created without sessionCode");
                return new ResponseEntity<>("Session initialize error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            logger.error("Session initialize failed - No session created");
            return new ResponseEntity<>("Session initialize failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Current design is the session will still valid within 1 hour after the session is ended for user to view the result
     * When user join the session with the url provided, there will be 3 possibility
     * 1. Invalid session - Session code doesn't exist in the system, throw http NOT_FOUND error code
     * 2. Invalid session - Session code exist, session_status = CLOSED, end_time > 1 hour - throw http NOT_FOUND error code
     * 3. Valid session - session code exists in Database
     * and (session_status = ACTIVE or (session_status = CLOSED & end_time < 1 hour))
     *
     * @param sessionCode
     * @return Session object if session code provided is valid
     * @return HttpStatus.NOT_FOUND if session code provided is invalid
     */
    @GetMapping("/{sessionCode}")
    public ResponseEntity<?> validateSession(@PathVariable String sessionCode) {
        List<Session> optionalSessionList = sessionService.findBySessionCodeOrderByStartTimeDesc(sessionCode);

        if (!optionalSessionList.isEmpty()) {
            Session session = optionalSessionList.get(0);
            if (session.getStatus().equals(SessionStatus.ACTIVE)
                || (session.getStatus().equals(SessionStatus.CLOSED)
                    && session.getEndTime().isAfter(LocalDateTime.now().minusHours(1)))) {

                return new ResponseEntity<>(session, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * @param sessionCode   - sessionCode for the session
     * @param userSessionId - used to verify if the requestor is the user who initiate the session,
     *                      userSessionID provided here should be the same userSessionId when initiate the session
     * @return - HttpStatus.OK & picked restaurant name if the session ended successfully
     * - HttpStatus.INTERNAL_SERVER_ERROR if session ended failed
     * - HttpStatus.BAD_REQUEST if the session requested already ended
     * - HttpStatus.UNAUTHORIZED if the userSessionId is not same as the initiatorUserSessionId
     * - HttpStatus.NOT_FOUND if the sessionCode provided is not exists in database
     */
    @GetMapping("/endSession/{sessionCode}")
    public ResponseEntity<?> endSession(@PathVariable String sessionCode, @RequestParam String userSessionId) {

        List<Session> optionalSessionList = sessionService.findActiveSessionBySessionCode(sessionCode);

        if (!optionalSessionList.isEmpty()) {
            Session session = optionalSessionList.get(0);

            if (session.getInitiatorUserSessionId().equalsIgnoreCase(userSessionId)) {
                if (session.getStatus().equals(SessionStatus.ACTIVE)) {

                    try {
                        restaurantService.randomPickARestaurantBySessionId(session.getId());
                        sessionService.endSession(session);
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                    catch (Exception e) {
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
