package wendy.tee.pickarestaurant.Service;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @Mock
    RestaurantService restaurantService;

    private LocalDateTime currentTime;
    private String sessionId;

    @BeforeEach
    public void setup(){
        currentTime = LocalDateTime.now();
        sessionId = "s9n10gm7PXKn";
    }

    @DisplayName("When call method createNewSession, should create a new session and return the new session")
    @Test
    public void shouldCreateNewSessionWhenCalled() {
        Session newSession = createSession();

        Mockito.when(sessionRepository.save(any())).thenReturn(newSession);

        Session createdSession = sessionService.createNewSession("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");

        assertThat(createdSession).isNotNull();
        assertThat(createdSession.getId()).isEqualTo(sessionId);
        assertThat(createdSession.getStatus()).isEqualTo(newSession.getStatus());
        assertThat(createdSession.getInitiatorUserSessionId()).isEqualTo(newSession.getInitiatorUserSessionId());
        assertThat(createdSession.getStartTime()).isEqualTo(newSession.getStartTime());
    }


    @DisplayName("When call method findById, should return a optional Session object.")
    @Test
    public void givenSessionIdWhenFindByIdShouldReturnSessionWithGivenSessionCode() {
        Session s1 = createSession();

        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(s1));

        Optional<Session> optionalSession = sessionService.findById(sessionId);

        Assertions.assertThat(optionalSession.isPresent()).isEqualTo(true);
        Assertions.assertThat(optionalSession.get()).isEqualTo(s1);
    }

    @DisplayName("When call method endSession, should update the status to CLOSED and end time to current time and return updated session.")
    @Test
    public void callEndSessionShouldReturnUpdatedSessionWithCLOSEDStatus() {
        LocalDateTime currentTime = LocalDateTime.now();

        Session s1 = createSession();
        Session s2 = createSession();
        s2.setStatus(SessionStatus.CLOSED);
        s2.setEndTime(currentTime);

        Mockito.when(sessionRepository.save(any())).thenReturn(s2);

        Session updatedSession = sessionService.endSession(s1);

        Assertions.assertThat(updatedSession.getStatus()).isEqualTo(SessionStatus.CLOSED);
        Assertions.assertThat(updatedSession.getEndTime()).isEqualTo(currentTime);
    }

    private Session createSession(){
        return Session.builder()
                            .id(sessionId)
                            .status(SessionStatus.ACTIVE)
                            .initiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej")
                            .startTime(currentTime)
                            .build();
    }
}
