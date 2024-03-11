package wendy.tee.pickarestaurant.Controller;

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
import java.util.ArrayList;
import java.util.List;

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

    @DisplayName("When initiate session, should return a new session")
    @Test
    public void whenInitiateSessionShouldReturnANewSessionWithStatusOK() throws Exception {
        LocalDateTime currentTime = LocalDateTime.now();
        int len = currentTime.toString().length();
        String currentTimeStr = currentTime.toString().substring(0, len - 2);

        Session newSession = new Session();
        newSession.setId(1L);
        newSession.setSessionCode("str1Ar4T54");
        newSession.setStatus(SessionStatus.ACTIVE);
        newSession.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        newSession.setStartTime(currentTime);

        Mockito.when(sessionService.createNewSession(any())).thenReturn(newSession);

        mockMvc.perform(get("/session/initiateSession"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.sessionCode").value("str1Ar4T54"))
               .andExpect(jsonPath("$.status").value("ACTIVE"))
               .andExpect(jsonPath("$.initiatorUserSessionId").value("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej"))
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

        Session session = new Session();
        Mockito.when(sessionService.createNewSession(any())).thenReturn(session);

        mockMvc.perform(get("/session/initiateSession"))
               .andExpect(status().isInternalServerError())
               .andDo(print());
    }

    @DisplayName("When validateSession, if sessionCode provided not exists in system, should return Http 404 NOT_FOUND")
    @Test
    public void whenValidateSessionWithInvalidSessionCodeShouldReturnHttp404() throws Exception {

        Mockito.when(sessionService.findActiveSessionBySessionCode(any())).thenReturn(new ArrayList<>());

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
        List<Session> sessionList = new ArrayList<>();
        Session s1 = new Session();
        s1.setId(1L);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.CLOSED);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now().minusHours(3));
        s1.setEndTime(LocalDateTime.now().minusHours(2));
        sessionList.add(s1);

        Mockito.when(sessionService.findActiveSessionBySessionCode(any())).thenReturn(sessionList);

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

        List<Session> sessionList = new ArrayList<>();
        Session s1 = new Session();
        s1.setId(1L);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.CLOSED);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now().minusHours(3));
        s1.setEndTime(LocalDateTime.now().minusMinutes(2));
        sessionList.add(s1);

        Mockito.when(sessionService.findBySessionCodeOrderByStartTimeDesc("str1Ar4T54")).thenReturn(sessionList);

        mockMvc.perform(get("/session/{sessionCode}", "str1Ar4T54"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.sessionCode").value("str1Ar4T54"))
               .andExpect(jsonPath("$.status").value("CLOSED"))
               .andExpect(jsonPath("$.initiatorUserSessionId").value("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej"))
               .andDo(print());
    }

    @DisplayName("When validateSession, if sessionCode provided exists in system, " +
                 "sessionStatus = 'ACTIVE', " +
                 "should return Session")
    @Test
    public void whenValidateSessionWhereStatusIsActiveShouldReturnListOfRestaurant() throws Exception {

        List<Session> sessionList = new ArrayList<>();
        Session s1 = new Session();
        s1.setId(1L);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.ACTIVE);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now());
        sessionList.add(s1);

        Mockito.when(sessionService.findBySessionCodeOrderByStartTimeDesc("str1Ar4T54")).thenReturn(sessionList);

        mockMvc.perform(get("/session/{sessionCode}", "str1Ar4T54"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.sessionCode").value("str1Ar4T54"))
               .andExpect(jsonPath("$.status").value("ACTIVE"))
               .andExpect(jsonPath("$.initiatorUserSessionId").value("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej"))
               .andDo(print());
    }

    @DisplayName("When endSession with a sessionCode that is not exists in system, should return Http 404 NOT_FOUND")
    @Test
    public void whenEndSessionWithInvalidSessionCodeShouldReturnHttp404NotFound() throws Exception {

        Mockito.when(sessionService.findActiveSessionBySessionCode(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/session/endSession/{sessionCode}", "test")
                                .param("userSessionId", "sampleParam"))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @DisplayName("Given userSessionId that is not the initiator of the session, when endSession, should return Http 401 UNAUTHORIZED")
    @Test
    public void givenUserSessionIdThatIsNotInitiatorWhenEndSessionShouldReturnHttp401Unauthorized() throws Exception {

        List<Session> sessionList = new ArrayList<>();
        Session s1 = new Session();
        s1.setId(1L);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.ACTIVE);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now().minusMinutes(20));
        sessionList.add(s1);

        Mockito.when(sessionService.findActiveSessionBySessionCode(any())).thenReturn(sessionList);

        mockMvc.perform(get("/session/endSession/{sessionCode}", "test")
                                .param("userSessionId", "sampleParam"))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

    @DisplayName("Given userSessionId that is not the initiator of the session, when endSession, should return Http 400 BAD_REQUEST")
    @Test
    public void givenSessionStatusIsClosedWhenEndSessionShouldReturnHttp400BadRequest() throws Exception {

        List<Session> sessionList = new ArrayList<>();
        Session s1 = new Session();
        s1.setId(1L);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.CLOSED);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now().minusMinutes(20));
        sessionList.add(s1);

        Mockito.when(sessionService.findActiveSessionBySessionCode(any())).thenReturn(sessionList);

        mockMvc.perform(get("/session/endSession/{sessionCode}", "test")
                                .param("userSessionId", "fnqwue3h1o8yr012porjnpqfnvoq283yr10iej"))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @DisplayName("Given exception happened when endSession, should return Http 500 INTERNAL_SERVER_ERROR")
    @Test
    public void givenExceptionHappenedWhenEndSessionShouldReturnHttp500InternalServerError() throws Exception {

        List<Session> sessionList = new ArrayList<>();
        Session s1 = new Session();
        s1.setId(1L);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.ACTIVE);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now().minusMinutes(20));
        sessionList.add(s1);

        Mockito.when(sessionService.findActiveSessionBySessionCode(any())).thenReturn(sessionList);
        Mockito.when(sessionService.endSession(s1)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/session/endSession/{sessionCode}", "test")
                                .param("userSessionId", "fnqwue3h1o8yr012porjnpqfnvoq283yr10iej"))
               .andExpect(status().isInternalServerError())
               .andDo(print());
    }

    @DisplayName("Given valid sessionCode and userSessionID, when endSession, should return Http 200 ok")
    @Test
    public void givenValidSessionCodeAndUserSessionIdWhenEndSessionShouldReturnHttp200ok() throws Exception {

        List<Session> sessionList = new ArrayList<>();
        Session s1 = new Session();
        s1.setId(1L);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.ACTIVE);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now().minusMinutes(20));
        sessionList.add(s1);

        Mockito.when(sessionService.findActiveSessionBySessionCode(any())).thenReturn(sessionList);

        mockMvc.perform(get("/session/endSession/{sessionCode}", "test")
                                .param("userSessionId", "fnqwue3h1o8yr012porjnpqfnvoq283yr10iej"))
               .andExpect(status().isOk())
               .andDo(print());
    }
}
