package wendy.tee.pickarestaurant.Service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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


    public Session createNewSession(String userSessionId) {
        Session newSession = new Session();
        newSession.setSessionCode(generateSessionCode());
        newSession.setStatus(SessionStatus.ACTIVE);
        newSession.setInitiatorUserSessionId(userSessionId);
        newSession.setStartTime(LocalDateTime.now());
        return sessionRepository.save(newSession);
    }

//    public SessionResponse convertSessionToSessionResponse(Session session){
//        SessionResponse response = new SessionResponse();
//        response.setId(session.getId());
//        response.setSessionCode(session.getSessionCode());
//        response.setStatus(session.getStatus());
//        return response;
//    }

    public List<Session> findActiveSessionBySessionCode(String sessionCode) {
        return sessionRepository.findBySessionCodeAndStatus(sessionCode, SessionStatus.ACTIVE);
    }

    public List<Session> findBySessionCode(String sessionCode) {
        return sessionRepository.findBySessionCodeOrderByEndTimeDesc(sessionCode);
    }

    public Optional<Session> findById(Long sessionId) {
        return sessionRepository.findById(sessionId);
    }

    public Session endSession(Session session) {
        session.setStatus(SessionStatus.CLOSED);
        session.setEndTime(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    private String generateSessionCode() {
        int length = 10;
        String generatedString = RandomStringUtils.random(length, true, true);
        return generatedString;
    }
}
