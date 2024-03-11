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
import wendy.tee.pickarestaurant.Model.Result;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Service.RestaurantService;
import wendy.tee.pickarestaurant.Service.ResultService;
import wendy.tee.pickarestaurant.Service.SessionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResultController.class)
public class ResultControllerTest {

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


    @DisplayName("Given valid sessionId and sessionStatus is ACTIVE, when getResultBySessionCode, should return Result")
    @Test
    public void giveValidSessionIdAndSessionStatusIsActiveWhenGetResultBySessionCodeShouldReturnResult() throws Exception {
        Mockito.when(sessionService.findById(sessionId)).thenReturn(Optional.of(activeSession));

        Result result = new Result();
        result.setId(10L);
        result.setSessionId(sessionId);
        result.setPickedRestaurantId(20L);
        result.setPickedRestaurantName("Danny's");

        Mockito.when(resultService.findBySessionId(sessionId)).thenReturn(result);

        mockMvc.perform(get("/result/{sessionCode}", sessionId))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id").value(10L))
               .andExpect(jsonPath("$.sessionId").value(sessionId))
               .andExpect(jsonPath("$.pickedRestaurantId").value(20L))
               .andExpect(jsonPath("$.pickedRestaurantName").value("Danny's"))
               .andDo(print());
    }

    @DisplayName("When getResultBySessionCode, if sessionCode provided exists in system, " +
                 "sessionStatus = 'CLOSED' and endTime < 1 hour from current time" +
                 "should return Result")
    @Test
    public void giveValidSessionCodeAndSessionStatusIsClosedEndTimeLessThan1HourFromNowWhenGetResultBySessionCodeShouldReturnResult() throws Exception {
        Session s1 = new Session();
        s1.setId(sessionId);
        s1.setStatus(SessionStatus.CLOSED);
        s1.setInitiatorUserSessionId(initiatorUserSessionId);
        s1.setStartTime(LocalDateTime.now().minusMinutes(30));
        s1.setEndTime(LocalDateTime.now().minusMinutes(10));

        Mockito.when(sessionService.findById(sessionId)).thenReturn(Optional.of(s1));

        Result result = new Result();
        result.setId(10L);
        result.setSessionId(sessionId);
        result.setPickedRestaurantId(20L);
        result.setPickedRestaurantName("Danny's");

        Mockito.when(resultService.findBySessionId(s1.getId())).thenReturn(result);

        mockMvc.perform(get("/result/{sessionCode}", sessionId))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id").value(10L))
               .andExpect(jsonPath("$.sessionId").value(sessionId))
               .andExpect(jsonPath("$.pickedRestaurantId").value(20L))
               .andExpect(jsonPath("$.pickedRestaurantName").value("Danny's"))
               .andDo(print());
    }

    @DisplayName("When getResultBySessionCode, if sessionCode provided exists in system, " +
                 "sessionStatus = 'CLOSED' and endTime > 1 hour from current time" +
                 "should return Http 400 NOT_FOUND")
    @Test
    public void giveValidSessionCodeAndSessionStatusIsClosedEndTimeMoreThan1HourFromNowWhenGetResultBySessionCodeShouldReturnHttp400NotFound() throws Exception {

        List<Session> sessionList = new ArrayList<>();
        Session s1 = new Session();
        s1.setId(sessionId);
        s1.setStatus(SessionStatus.CLOSED);
        s1.setInitiatorUserSessionId(initiatorUserSessionId);
        s1.setStartTime(LocalDateTime.now().minusHours(11));
        s1.setEndTime(LocalDateTime.now().minusHours(10));
        sessionList.add(s1);

        Mockito.when(sessionService.findById(sessionId)).thenReturn(Optional.of(s1));

        mockMvc.perform(get("/result/{sessionCode}", sessionId))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @DisplayName("When getResultBySessionCode, if sessionCode provided NOT exists in system, " +
                 "should return Http 400 NOT_FOUND")
    @Test
    public void giveSessionCodeThatNotExistInDatabaseWhenGetResultBySessionCodeShouldReturnHttp400NotFound() throws Exception {
        Mockito.when(sessionService.findById(sessionId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/result/{sessionCode}", sessionId))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }
}
