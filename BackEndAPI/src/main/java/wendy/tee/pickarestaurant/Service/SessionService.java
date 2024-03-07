package wendy.tee.pickarestaurant.Service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Repository.SessionRepository;
import wendy.tee.pickarestaurant.Enum.SessionStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    SessionRepository sessionRepository;

    public Session createNewSession(){
        Session newSession = new Session();
        newSession.setSessionCode(generateSessionCode());
        newSession.setStatus(SessionStatus.ACTIVE);
        newSession.setStartTime(LocalDateTime.now());
        return sessionRepository.save(newSession);
    }

    public List<Session> findActiveSessionBySessionCode(String sessionCode){
        return sessionRepository.findBySessionCodeAndStatus(sessionCode, SessionStatus.ACTIVE);
    }

    public Session endSession(Session session){
        session.setStatus(SessionStatus.CLOSED);
        session.setEndTime(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    private String generateSessionCode(){
        int length = 10;
        String generatedString = RandomStringUtils.random(length, true, true);
        return generatedString;
    }
}
