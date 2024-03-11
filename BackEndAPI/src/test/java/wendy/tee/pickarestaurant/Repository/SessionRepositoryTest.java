package wendy.tee.pickarestaurant.Repository;

import org.apache.commons.lang3.RandomStringUtils;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
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

    private final LocalDateTime currentTime = LocalDateTime.now();

    Session newSession;

    @BeforeEach
    public void setup(){
        newSession = new Session();
        newSession.setId("s9n10gm7PXKn");
        newSession.setStatus(SessionStatus.ACTIVE);
        newSession.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        newSession.setStartTime(currentTime);
    }

    @DisplayName("Given new Session, when save, should save the session and return the saved session")
    @Test
    public void givenNewSessionWhenSaveThenSavedSuccess() {
        Session savedSession = sessionRepository.save(newSession);

        assertThat(savedSession).hasFieldOrPropertyWithValue("id", "s9n10gm7PXKn");
        assertThat(savedSession).hasFieldOrPropertyWithValue("status", SessionStatus.ACTIVE);
        assertThat(savedSession).hasFieldOrPropertyWithValue("initiatorUserSessionId", "fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        assertThat(savedSession).hasFieldOrPropertyWithValue("startTime", currentTime);
    }

    @DisplayName("Given updated Session, when save, should update data to database")
    @Test
    public void givenUpdatedSessionWhenSaveShouldSaveUpdatedSession() {
        entityManager.persist(newSession);

        Session dbSession = sessionRepository.findById(newSession.getId()).get();

        dbSession.setStatus(SessionStatus.CLOSED);
        dbSession.setEndTime(LocalDateTime.now());

        sessionRepository.save(dbSession);

        Session updatedSession = sessionRepository.findById(dbSession.getId()).get();
        assertThat(updatedSession.getId()).isEqualTo(dbSession.getId());
        assertThat(updatedSession.getStatus()).isEqualTo(dbSession.getStatus());
        assertThat(updatedSession.getInitiatorUserSessionId()).isEqualTo(dbSession.getInitiatorUserSessionId());
        assertThat(updatedSession.getEndTime()).isEqualTo(dbSession.getEndTime());
    }

    @DisplayName("Given updated Session, when save, should update data to database")
    @Test
    public void giveSessionIdWhenFindByIdShouldReturnRequestedSession() {
        entityManager.persist(newSession);

        Session dbSession = sessionRepository.findById(newSession.getId()).get();

        assertThat(dbSession).hasFieldOrPropertyWithValue("id", "s9n10gm7PXKn");
        assertThat(dbSession).hasFieldOrPropertyWithValue("status", SessionStatus.ACTIVE);
        assertThat(dbSession).hasFieldOrPropertyWithValue("initiatorUserSessionId", "fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        assertThat(dbSession).hasFieldOrPropertyWithValue("startTime", currentTime);
    }




}
