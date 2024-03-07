package wendy.tee.pickarestaurant.Controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import wendy.tee.pickarestaurant.Enum.SessionStatus;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Service.RestaurantService;
import wendy.tee.pickarestaurant.Service.SessionService;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {


    @MockBean
    SessionService sessionService;

    @MockBean
    RestaurantService restaurantService;

    @Autowired
    MockMvc mockMvc;

    @DisplayName("When initiate session, should return a new session")
    @Test
    public void whenInitiateSessionShouldReturnANewSessionWithStatusOK() throws Exception {
        LocalDateTime currentTime = LocalDateTime.now();
        int len = currentTime.toString().length();
        String currentTimeStr = currentTime.toString().substring(0, len-2);
        String sessionCode = "str1Ar4T54";

        Session newSession = new Session();
        newSession.setId(1l);
        newSession.setSessionCode(sessionCode);
        newSession.setStatus(SessionStatus.ACTIVE);
        newSession.setStartTime(currentTime);

        when(sessionService.createNewSession()).thenReturn(newSession);

        mockMvc.perform(get("/session"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1l))
               .andExpect(jsonPath("$.sessionCode").value(sessionCode))
               .andExpect(jsonPath("$.status").value("ACTIVE"))
               .andExpect(jsonPath("$.startTime").value(currentTimeStr))
               .andDo(print());
    }

    @DisplayName("When initiate session failed, should http 500(Internal Server Error)")
    @Test
    public void whenInitiateSessionFailedShouldReturnHttpStatusCode500() {

    }
}
