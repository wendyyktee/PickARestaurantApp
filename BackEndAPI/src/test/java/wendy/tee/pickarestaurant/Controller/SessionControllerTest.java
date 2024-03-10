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
import wendy.tee.pickarestaurant.Model.Restaurant;
import wendy.tee.pickarestaurant.Model.Result;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Service.RestaurantService;
import wendy.tee.pickarestaurant.Service.ResultService;
import wendy.tee.pickarestaurant.Service.SessionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        newSession.setId(1l);
        newSession.setSessionCode("str1Ar4T54");
        newSession.setStatus(SessionStatus.ACTIVE);
        newSession.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        newSession.setStartTime(currentTime);

        Mockito.when(sessionService.createNewSession(any())).thenReturn(newSession);

        mockMvc.perform(get("/session"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1l))
               .andExpect(jsonPath("$.sessionCode").value("str1Ar4T54"))
               .andExpect(jsonPath("$.status").value("ACTIVE"))
               .andExpect(jsonPath("$.initiatorHttpSessionId").value("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej"))
               .andExpect(jsonPath("$.startTime").value(currentTimeStr))
               .andDo(print());
    }

    @DisplayName("When initiate session failed, should return HTTP 500 Internal Server Error")
    @Test
    public void whenInitiateSessionFailedShouldReturnHTTP500() throws Exception {
        Mockito.when(sessionService.createNewSession(any())).thenReturn(null);

        mockMvc.perform(get("/session"))
               .andExpect(status().is5xxServerError())
               .andDo(print());

        Session session = new Session();
        Mockito.when(sessionService.createNewSession(any())).thenReturn(session);

        mockMvc.perform(get("/session"))
               .andExpect(status().isInternalServerError())
               .andDo(print());
    }

    @DisplayName("When joinSession, if sessionCode provided not exists in system, should return Http 404 NOT_FOUND")
    @Test
    public void whenJoinSessionWithInvalidSessionCodeShouldReturnHttp404() throws Exception {

        Mockito.when(sessionService.findActiveSessionBySessionCode(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/session/{sessionCode}", "test"))
               .andExpect(status().isNotFound())
               .andDo(print());
    }

    @DisplayName("When joinSession, if sessionCode provided exists in system, " +
                 "sessionStatus = 'CLOSED', " +
                 "endTime > 1 hour from current time, " +
                 "should return Http 404 NOT_FOUND")
    @Test
    public void whenJoinSessionWhereStatusIsClosedAndEndedMoreThan1HourShouldReturnHttp404() throws Exception {
        List<Session> sessionList = new ArrayList<>();
        Session s1 = new Session();
        s1.setId(1l);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.CLOSED);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now().minusHours(3));
        s1.setEndTime(LocalDateTime.now().minusHours(2));
        sessionList.add(s1);

        Mockito.when(sessionService.findActiveSessionBySessionCode(any())).thenReturn(sessionList);

        mockMvc.perform(post("/session/{sessionCode}", "test"))
               .andExpect(status().isNotFound())
               .andDo(print());
    }

    @DisplayName("When joinSession, if sessionCode provided exists in system, " +
                 "sessionStatus = 'CLOSED', " +
                 "endTime < 1 hour from current time, " +
                 "should return object Result")
    @Test
    public void whenJoinSessionWhereStatusIsClosedAndEndedLessThan1HourShouldReturnResult() throws Exception {

        List<Session> sessionList = new ArrayList<>();
        Session s1 = new Session();
        s1.setId(1l);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.CLOSED);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now().minusHours(3));
        s1.setEndTime(LocalDateTime.now().minusMinutes(2));
        sessionList.add(s1);

        Mockito.when(sessionService.findActiveSessionBySessionCode(any())).thenReturn(sessionList);

        Result result = new Result();
        result.setId(1l);
        result.setSessionId(20l);
        result.setPickedRestaurantId(30l);
        result.setPickedRestaurantName("Danny's");

        Mockito.when(resultService.findBySessionId(any())).thenReturn(result);

        mockMvc.perform(post("/session/{sessionCode}", "str1Ar4T54"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.pickedRestaurantName").value("Danny's"))
               .andDo(print());
    }

    @DisplayName("When joinSession, if sessionCode provided exists in system, " +
                 "sessionStatus = 'ACTIVE', " +
                 "should return object Result")
    @Test
    public void whenJoinSessionWhereStatusIsActiveShouldReturnListOfRestaurant() throws Exception {

        List<Session> sessionList = new ArrayList<>();
        Session s1 = new Session();
        s1.setId(1l);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.ACTIVE);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now());
        sessionList.add(s1);

        Mockito.when(sessionService.findActiveSessionBySessionCode(any())).thenReturn(sessionList);

        List<Restaurant> restaurantList = new ArrayList<>();
        Restaurant r1 = new Restaurant();
        r1.setId(300l);
        r1.setSessionId(20l);
        r1.setRestaurantName("KFF");

        Restaurant r2 = new Restaurant();
        r2.setId(100l);
        r2.setSessionId(20l);
        r2.setRestaurantName("Danny's");
        restaurantList.add(r1);
        restaurantList.add(r2);

        Mockito.when(restaurantService.findBySessionId(any())).thenReturn(restaurantList);

        mockMvc.perform(post("/session/{sessionCode}", "str1Ar4T54"))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.*", isA(ArrayList.class)))
               .andExpect(jsonPath("$.*", hasSize(2)))
               .andExpect(jsonPath("$[*].id", containsInAnyOrder(100, 300)))
               .andExpect(jsonPath("$[*].sessionId", containsInAnyOrder(20, 20)))
               .andExpect(jsonPath("$[*].restaurantName", containsInAnyOrder("KFF", "Danny's")))
               .andDo(print());
    }
}
