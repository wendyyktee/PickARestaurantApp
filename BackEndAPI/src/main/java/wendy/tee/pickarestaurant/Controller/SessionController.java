package wendy.tee.pickarestaurant.Controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wendy.tee.pickarestaurant.Enum.SessionStatus;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Service.SessionService;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/session")
@SessionAttributes("sessionKey")
public class SessionController {

    @Autowired
    SessionService sessionService;

    private Logger logger = LoggerFactory.getLogger(
            SessionController.class);

    /**
     * To start a new session, front-end application will send userSessionId to persist the session, which will be used
     * to verification when end session     *
     *
     * @param httpSession - get id from http session for identify initiator use
     * @return - Session object if session initiate successfully
     * @throws - HttpStatus.INTERNAL_SERVER_ERROR if session initiate failed
     */
    @GetMapping(value = "/initiateSession", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> initiateSession(HttpSession httpSession) {
        Session session = sessionService.createNewSession(httpSession.getId());

        if (session != null) {
            return new ResponseEntity<>(session, HttpStatus.OK);
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
     * @param sessionId - Session.id
     * @return - Session object if session code provided is valid
     * @return - HttpStatus.BAD_REQUEST if the session provided is not exists in database or already ended
     */
    @GetMapping(value = "/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validateSession(@PathVariable String sessionId) {
        Optional<Session> optionalSession = sessionService.findById(sessionId);

        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            if (session.getStatus().equals(SessionStatus.ACTIVE)
                || (session.getStatus().equals(SessionStatus.CLOSED)
                    && session.getEndTime().isAfter(LocalDateTime.now().minusHours(1)))) {

                return new ResponseEntity<>(session, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * @param sessionId     - sessionCode for the session
     * @param userSessionId - used to verify if the requestor is the user who initiate the session,
     *                      userSessionID provided here should be the same userSessionId when initiate the session
     * @return - HttpStatus.OK & picked restaurant name if the session ended successfully
     * - HttpStatus.INTERNAL_SERVER_ERROR if session ended failed
     * - HttpStatus.BAD_REQUEST if the session provided is not exists in database or already ended
     * - HttpStatus.UNAUTHORIZED if the userSessionId is not same as the initiatorUserSessionId
     */
    @GetMapping(value = "/endSession/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> endSession(@PathVariable String sessionId, @RequestParam String userSessionId) {
        Optional<Session> optionalSession = sessionService.findById(sessionId);

        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();

            if (session.getInitiatorUserSessionId().equalsIgnoreCase(userSessionId)) {
                if (session.getStatus().equals(SessionStatus.ACTIVE)) {

                    try {
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
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
