package wendy.tee.pickarestaurant.Repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import wendy.tee.pickarestaurant.Enum.SessionStatus;
import wendy.tee.pickarestaurant.Model.Session;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SessionRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    SessionRepository sessionRepository;

    @Test
    public void giveNewSessionWhenSaveThenSavedSuccess() {
        String sessionCode = "str1Ar4T54";
        LocalDateTime now = LocalDateTime.now();

        Session newSession = new Session();
        newSession.setSessionCode(sessionCode);
        newSession.setStatus(SessionStatus.ACTIVE);
        newSession.setStartTime(now);

        Session savedSession = sessionRepository.save(newSession);

        assertThat(savedSession).hasFieldOrPropertyWithValue("sessionCode", sessionCode);
        assertThat(savedSession).hasFieldOrPropertyWithValue("status", SessionStatus.ACTIVE);
        assertThat(savedSession).hasFieldOrPropertyWithValue("startTime", now);
    }

    @DisplayName("When call findBySessionCodeAndStatus, should return list of Session with corresponding session code with status = SessionStatus.ACTIVE")
    @Test
    public void givenSessionCodeWhenFindSessionShouldReturnCorrectSession() {
        String sessionCode = "str1Ar4T62";
        Session s1 = new Session();
        s1.setSessionCode(sessionCode);
        s1.setStatus(SessionStatus.ACTIVE);
        s1.setStartTime(LocalDateTime.now());
        entityManager.persist(s1);

        Session s2 = new Session();
        s2.setSessionCode("trd1Ar4T62");
        s2.setStatus(SessionStatus.ACTIVE);
        s2.setStartTime(LocalDateTime.now());
        entityManager.persist(s2);

        Session s3 = new Session();
        s3.setSessionCode("sec1Ar4T62");
        s3.setStatus(SessionStatus.ACTIVE);
        s3.setStartTime(LocalDateTime.now());
        entityManager.persist(s3);

        List<Session> foundSession = sessionRepository.findBySessionCodeAndStatus(sessionCode, SessionStatus.ACTIVE);

        assertThat(foundSession).hasSize(1);
        assertThat(foundSession).contains(s1);
    }

    @Test
    public void givenUpdatedSessionShouldSaveUpdatedSession(){
        String sessionCode = "str1Ar4T62";
        Session s1 = new Session();
        s1.setSessionCode(sessionCode);
        s1.setStatus(SessionStatus.ACTIVE);
        s1.setStartTime(LocalDateTime.now());
        entityManager.persist(s1);

        Session updatedSession = sessionRepository.findById(s1.getId()).get();
        updatedSession.setStatus(SessionStatus.CLOSED);
        updatedSession.setEndTime(LocalDateTime.now());
        sessionRepository.save(updatedSession);


        Session currentDbSession = sessionRepository.findById(s1.getId()).get();
        assertThat(currentDbSession.getId()).isEqualTo(s1.getId());
        assertThat(currentDbSession.getStatus()).isEqualTo(updatedSession.getStatus());
        assertThat(currentDbSession.getEndTime()).isEqualTo(updatedSession.getEndTime());
    }
}
