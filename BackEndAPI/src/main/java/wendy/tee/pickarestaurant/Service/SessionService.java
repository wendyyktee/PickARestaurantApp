package wendy.tee.pickarestaurant.Service;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wendy.tee.pickarestaurant.Controller.SessionController;
import wendy.tee.pickarestaurant.Enum.SessionStatus;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    RestaurantService restaurantService;

    private Logger logger = LoggerFactory.getLogger(
            SessionService.class);

    public Session createNewSession(String userSessionId) {
        Session newSession = new Session();
        newSession.setId(generateUniqueSessionId());
        newSession.setStatus(SessionStatus.ACTIVE);
        newSession.setInitiatorUserSessionId(userSessionId);
        newSession.setStartTime(LocalDateTime.now());
        logger.info("New session created with id = " + newSession.getId());
        return sessionRepository.save(newSession);
    }

    public Optional<Session> findById(String sessionId) {
        return sessionRepository.findById(sessionId);
    }

    @Transactional
    public Session endSession(Session session) {
        restaurantService.randomPickARestaurantBySessionId(session.getId());
        session.setStatus(SessionStatus.CLOSED);
        session.setEndTime(LocalDateTime.now());
        logger.info("Ended session with id = " + session.getId());
        return sessionRepository.save(session);
    }

    private String generateUniqueSessionId() {
        String generatedId = RandomStringUtils.random(12, true, true);

        if(sessionRepository.findById(generatedId).isPresent()){
             return generateUniqueSessionId();
        }
        else{
            return generatedId;
        }
    }
}
