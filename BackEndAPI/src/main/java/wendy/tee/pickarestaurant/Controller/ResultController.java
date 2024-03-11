package wendy.tee.pickarestaurant.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wendy.tee.pickarestaurant.Enum.SessionStatus;
import wendy.tee.pickarestaurant.Model.Result;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Service.ResultService;
import wendy.tee.pickarestaurant.Service.SessionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/result")
public class ResultController {

    @Autowired
    ResultService resultService;

    @Autowired
    SessionService sessionService;


    /**
     *
     * @param sessionId - Session.id
     * @return - Result
     * @return - HttpStatus.BAD_REQUEST if the session provided is not exists in database or already ended
     */
    @GetMapping(value = "/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getResultBySessionCode(@PathVariable String sessionId) {
        Optional<Session> optionalSession = sessionService.findById(sessionId);

        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            if (session.getStatus().equals(SessionStatus.ACTIVE)
                || (session.getStatus().equals(SessionStatus.CLOSED)
                    && session.getEndTime().isAfter(LocalDateTime.now().minusHours(1)))) {

                Result result = resultService.findBySessionId(session.getId());
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
