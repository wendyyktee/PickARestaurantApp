package wendy.tee.pickarestaurant.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wendy.tee.pickarestaurant.Enum.SessionStatus;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Service.RestaurantService;
import wendy.tee.pickarestaurant.Service.ResultService;
import wendy.tee.pickarestaurant.Service.SessionService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {

    @MockBean
    SessionService sessionService;

    @MockBean
    RestaurantService restaurantService;

    @MockBean
    ResultService resultService;

    @Autowired
    MockMvc mockMvc;

    private LocalDateTime currentTime;
    private String sessionId;
    private String initiatorUserSessionId;
    private Session activeSession;

    @BeforeEach
    public void setup() {
        currentTime = LocalDateTime.now();
        sessionId = "s9n10gm7PXKn";
        initiatorUserSessionId = "fnqwue3h1o8yr012porjnpqfnvoq283yr10iej";
        activeSession = Session.builder()
                               .id(sessionId)
                               .status(SessionStatus.ACTIVE)
                               .initiatorUserSessionId(initiatorUserSessionId)
                               .startTime(currentTime)
                               .build();
    }

    @DisplayName("When initiate session, should return a new session")
    @Test
    public void whenInitiateSessionShouldReturnANewSessionWithStatusOK() throws Exception {
        int len = currentTime.toString().length();
        String currentTimeStr = currentTime.toString().substring(0, len - 2);

        Mockito.when(sessionService.createNewSession(any())).thenReturn(activeSession);

        mockMvc.perform(get("/session/initiateSession"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id").value(sessionId))
               .andExpect(jsonPath("$.status").value("ACTIVE"))
               .andExpect(jsonPath("$.initiatorUserSessionId").value(initiatorUserSessionId))
               .andExpect(jsonPath("$.startTime").value(currentTimeStr))
               .andDo(print());
    }

    @DisplayName("When initiate session failed, should return HTTP 500 Internal Server Error")
    @Test
    public void whenInitiateSessionFailedShouldReturnHTTP500() throws Exception {
        Mockito.when(sessionService.createNewSession(any())).thenReturn(null);

        mockMvc.perform(get("/session/initiateSession"))
               .andExpect(status().is5xxServerError())
               .andDo(print());
    }

    @DisplayName("When validateSession, if sessionId provided not exists in system, should return Http 404 NOT_FOUND")
    @Test
    public void whenValidateSessionWithInvalidSessionCodeShouldReturnHttp404() throws Exception {

        Mockito.when(sessionService.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/session/{sessionCode}", "test"))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @DisplayName("When validateSession, if sessionCode provided exists in system, but" +
                 "sessionStatus = 'CLOSED', " +
                 "endTime > 1 hour from current time, " +
                 "should return Http 404 NOT_FOUND")
    @Test
    public void whenValidateSessionWhereStatusIsClosedAndEndedMoreThan1HourShouldReturnHttp404() throws Exception {
        Session session = new Session();
        session.setId(sessionId);
        session.setStatus(SessionStatus.CLOSED);
        session.setInitiatorUserSessionId(initiatorUserSessionId);
        session.setStartTime(LocalDateTime.now().minusHours(3));
        session.setEndTime(LocalDateTime.now().minusHours(2));

        Mockito.when(sessionService.findById(any())).thenReturn(Optional.of(session));

        mockMvc.perform(get("/session/{sessionCode}", "test"))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @DisplayName("When validateSession, if sessionCode provided exists in system, " +
                 "sessionStatus = 'CLOSED', " +
                 "endTime < 1 hour from current time, " +
                 "should return Session")
    @Test
    public void whenValidateSessionWhereStatusIsClosedAndEndedLessThan1HourShouldReturnSession() throws Exception {
        Session session = new Session();
        session.setId(sessionId);
        session.setStatus(SessionStatus.CLOSED);
        session.setInitiatorUserSessionId(initiatorUserSessionId);
        session.setStartTime(LocalDateTime.now().minusHours(3));
        session.setEndTime(LocalDateTime.now().minusMinutes(2));

        Mockito.when(sessionService.findById(sessionId)).thenReturn(Optional.of(session));

        mockMvc.perform(get("/session/{sessionCode}", sessionId))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id").value(sessionId))
               .andExpect(jsonPath("$.status").value("CLOSED"))
               .andExpect(jsonPath("$.initiatorUserSessionId").value(initiatorUserSessionId))
               .andDo(print());
    }

    @DisplayName("When validateSession, if sessionCode provided exists in system, " +
                 "sessionStatus = 'ACTIVE', " +
                 "should return Session")
    @Test
    public void whenValidateSessionWhereStatusIsActiveShouldReturnListOfRestaurant() throws Exception {
        Mockito.when(sessionService.findById(sessionId)).thenReturn(Optional.of(activeSession));

        mockMvc.perform(get("/session/{sessionCode}", sessionId))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id").value(sessionId))
               .andExpect(jsonPath("$.status").value("ACTIVE"))
               .andExpect(jsonPath("$.initiatorUserSessionId").value(initiatorUserSessionId))
               .andDo(print());
    }

    @DisplayName("When endSession with a sessionCode that is not exists in system, should return Http 404 NOT_FOUND")
    @Test
    public void whenEndSessionWithInvalidSessionCodeShouldReturnHttp404NotFound() throws Exception {

        Mockito.when(sessionService.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/session/endSession/{sessionCode}", "test")
                                .param("userSessionId", "sampleParam"))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @DisplayName("Given userSessionId that is not the initiator of the session, when endSession, should return Http 401 UNAUTHORIZED")
    @Test
    public void givenUserSessionIdThatIsNotInitiatorWhenEndSessionShouldReturnHttp401Unauthorized() throws Exception {
        Session session = new Session();
        session.setId(sessionId);
        session.setStatus(SessionStatus.ACTIVE);
        session.setInitiatorUserSessionId(initiatorUserSessionId);
        session.setStartTime(currentTime);

        Mockito.when(sessionService.findById(sessionId)).thenReturn(Optional.of(session));

        mockMvc.perform(get("/session/endSession/{sessionCode}", sessionId)
                                .param("userSessionId", "sampleParam"))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

    @DisplayName("Given session status is Closed, when endSession, should return Http 400 BAD_REQUEST")
    @Test
    public void givenSessionStatusIsClosedWhenEndSessionShouldReturnHttp400BadRequest() throws Exception {
        Session session = new Session();
        session.setId(sessionId);
        session.setStatus(SessionStatus.CLOSED);
        session.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        session.setStartTime(currentTime);

        Mockito.when(sessionService.findById(sessionId)).thenReturn(Optional.of(session));

        mockMvc.perform(get("/session/endSession/{sessionCode}", "test")
                                .param("userSessionId", "fnqwue3h1o8yr012porjnpqfnvoq283yr10iej"))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @DisplayName("Given exception happened when endSession, should return Http 500 INTERNAL_SERVER_ERROR")
    @Test
    public void givenExceptionHappenedWhenEndSessionShouldReturnHttp500InternalServerError() throws Exception {

        Mockito.when(sessionService.findById(any())).thenReturn(Optional.of((activeSession)));
        Mockito.when(sessionService.endSession(activeSession)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/session/endSession/{sessionCode}", "test")
                                .param("userSessionId", "fnqwue3h1o8yr012porjnpqfnvoq283yr10iej"))
               .andExpect(status().isInternalServerError())
               .andDo(print());
    }

    @DisplayName("Given valid sessionCode and userSessionID, when endSession, should return Http 200 ok")
    @Test
    public void givenValidSessionCodeAndUserSessionIdWhenEndSessionShouldReturnHttp200ok() throws Exception {
        Mockito.when(sessionService.findById(any())).thenReturn(Optional.of((activeSession)));

        mockMvc.perform(get("/session/endSession/{sessionCode}", "test")
                                .param("userSessionId", "fnqwue3h1o8yr012porjnpqfnvoq283yr10iej"))
               .andExpect(status().isOk())
               .andDo(print());
    }
}
