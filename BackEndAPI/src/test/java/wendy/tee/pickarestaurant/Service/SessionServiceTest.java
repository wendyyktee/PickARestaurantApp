package wendy.tee.pickarestaurant.Service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import wendy.tee.pickarestaurant.Enum.SessionStatus;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    SessionRepository sessionRepository;

    @InjectMocks
    SessionService sessionService;


    @DisplayName("When call method createNewSession, should create a new session and return the new session")
    @Test
    public void shouldCreateNewSessionWhenCalled() {
        LocalDateTime currentTime = LocalDateTime.now();

        Session newSession = new Session();
        newSession.setId(1l);
        newSession.setSessionCode("str1Ar4T54");
        newSession.setStatus(SessionStatus.ACTIVE);
        newSession.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        newSession.setStartTime(currentTime);

        Mockito.when(sessionRepository.save(any())).thenReturn(newSession);

        Session session = sessionService.createNewSession("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");

        assertThat(session).isNotNull();
        assertThat(session.getId()).isEqualTo(1l);
        assertThat(session.getStatus()).isEqualTo(SessionStatus.ACTIVE);
        assertThat(session.getStartTime()).isEqualTo(currentTime);

    }

    @DisplayName("When call method findSessionBySessionCode, should return a corresponding session.")
    @Test
    public void givenSessionCodeWhenFindSessionShouldReturnCorrectSession() {
        String sessionCode = "str1Ar4T62";
        List<Session> list1 = new ArrayList<>();
        Session s1 = new Session();
        s1.setSessionCode(sessionCode);
        s1.setStatus(SessionStatus.ACTIVE);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now());
        list1.add(s1);

        Mockito.when(sessionRepository.findBySessionCodeAndStatus(any(), any())).thenReturn(list1);

        List<Session> foundSession = sessionService.findActiveSessionBySessionCode(sessionCode);

        Assertions.assertThat(foundSession).hasSize(1);
        Assertions.assertThat(foundSession).contains(s1);
    }

    @DisplayName("When call method findById, should return a optional Session object.")
    @Test
    public void givenSessionIdWhenFindByIdShouldReturnCorrectSession() {
        Session s1 = new Session();
        s1.setId(1l);
        s1.setSessionCode("str1Ar4T62");
        s1.setStatus(SessionStatus.ACTIVE);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now());

        Mockito.when(sessionRepository.findById(1l)).thenReturn(Optional.of(s1));

        Optional<Session> optionalSession = sessionService.findById(1l);

        Assertions.assertThat(optionalSession.isPresent()).isEqualTo(true);
        Assertions.assertThat(optionalSession.get()).isEqualTo(s1);
    }

    @DisplayName("When call method endSession, should update the status to CLOSED and end time to current time and return updated session.")
    @Test
    public void callEndSessionShouldReturnUpdatedSessionWithCLOSEDStatus() {
        LocalDateTime currentTime = LocalDateTime.now();

        Session s1 = Session.builder()
                            .id(1l)
                            .sessionCode("testing123")
                            .status(SessionStatus.ACTIVE)
                            .initiatorHttpSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej")
                            .startTime(currentTime.minusMinutes(1))
                            .build();

        Session s2 = Session.builder()
                            .id(1l)
                            .sessionCode("testing123")
                            .status(SessionStatus.CLOSED)
                            .initiatorHttpSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej")
                            .startTime(currentTime.minusMinutes(1))
                            .endTime(currentTime)
                            .build();

        Mockito.when(sessionRepository.save(any())).thenReturn(s2);
        Session updatedSession = sessionService.endSession(s1);

        Assertions.assertThat(updatedSession.getStatus()).isEqualTo(SessionStatus.CLOSED);
        Assertions.assertThat(updatedSession.getEndTime()).isEqualTo(currentTime);
    }
}
