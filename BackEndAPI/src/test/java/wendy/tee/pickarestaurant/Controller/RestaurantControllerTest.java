package wendy.tee.pickarestaurant.Controller;

import org.json.JSONObject;
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
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Service.RestaurantService;
import wendy.tee.pickarestaurant.Service.ResultService;
import wendy.tee.pickarestaurant.Service.SessionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    @MockBean
    SessionService sessionService;

    @MockBean
    RestaurantService restaurantService;

    @MockBean
    ResultService resultService;

    @Autowired
    MockMvc mockMvc;

    @DisplayName("Given sessionId, when getRestaurantBySessionCode, " +
                 "should return list of restaurant with corresponding sessionId and http status OK")
    @Test
    public void givenValidSessionIdWhenGetRestaurantBySessionCodeShouldReturnListOfRestaurantWithHttp200ok() throws Exception {
        Session s1 = new Session();
        s1.setId(2222L);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.ACTIVE);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now().minusMinutes(5));

        Mockito.when(sessionService.findById(2222L)).thenReturn(Optional.of(s1));

        List<Restaurant> restaurantList = getRestaurantList();

        Mockito.when(restaurantService.findBySessionId(2222L)).thenReturn(restaurantList);

        mockMvc.perform(get("/restaurant").param("sessionId", "2222").contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.*", isA(ArrayList.class)))
               .andExpect(jsonPath("$.*", hasSize(3)))
               .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2, 3)))
               .andExpect(jsonPath("$[*].sessionId", containsInAnyOrder(2222, 2222, 2222)))
               .andExpect(jsonPath("$[*].restaurantName", containsInAnyOrder("KFF", "Res01", "Cafe01")))
               .andDo(print());
    }

    private static List<Restaurant> getRestaurantList() {
        List<Restaurant> restaurantList = new ArrayList<>();
        Restaurant r1 = new Restaurant();
        r1.setId(1L);
        r1.setRestaurantName("KFF");
        r1.setSessionId(2222L);
        restaurantList.add(r1);

        Restaurant r2 = new Restaurant();
        r2.setId(2L);
        r2.setRestaurantName("Res01");
        r2.setSessionId(2222L);
        restaurantList.add(r2);

        Restaurant r3 = new Restaurant();
        r3.setId(3L);
        r3.setRestaurantName("Cafe01");
        r3.setSessionId(2222L);
        restaurantList.add(r3);
        return restaurantList;
    }

    @DisplayName("Give Restaurant with invalid sessionId, when getRestaurantBySessionCode, " +
                 "should return Http 400 BAD_REQUEST")
    @Test
    public void givenInvalidSessionIdWhenGetRestaurantBySessionCodeShouldReturnHttp400BadRequest() throws Exception {
        Mockito.when(sessionService.findById(1L)).thenReturn(Optional.empty());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 20L);
        jsonObject.put("restaurantName", "Cafe100");
        jsonObject.put("sessionId", 1L);

        mockMvc.perform(put("/restaurant/addRestaurant").content(jsonObject.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @DisplayName("Give Restaurant with valid sessionId, but sessionStatus = CLOSED, when getRestaurantBySessionCode, " +
                 "should return Http 404 BAD_REQUEST")
    @Test
    public void givenValidSessionAndSessionStatusIsClosedWhenGetRestaurantBySessionCodeShouldReturnHttp400BadRequest() throws Exception {
        Session s1 = new Session();
        s1.setId(1L);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.CLOSED);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now().minusMinutes(5));
        Mockito.when(sessionService.findById(1L)).thenReturn(Optional.of(s1));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 20L);
        jsonObject.put("restaurantName", "Cafe100");
        jsonObject.put("sessionId", 1L);

        mockMvc.perform(put("/restaurant/addRestaurant").content(jsonObject.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @DisplayName("Give Restaurant with valid sessionId & sessionStatus = 'ACTIVE', when addRestaurant, " +
                 "should save the new Restaurant and return OK")
    @Test
    public void givenRestaurantWithValidSessionIdAndSessionStatusIsActiveWhenAddRestaurantShouldReturnHttp200ok() throws Exception {
        Session s1 = new Session();
        s1.setId(1L);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.ACTIVE);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now().minusMinutes(5));

        Mockito.when(sessionService.findById(1L)).thenReturn(Optional.of(s1));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 20L);
        jsonObject.put("restaurantName", "Cafe100");
        jsonObject.put("sessionId", 1L);

        mockMvc.perform(put("/restaurant/addRestaurant").content(jsonObject.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @DisplayName("Given Restaurant with invalid sessionId, when addRestaurant, " +
                 "should return Http 404 BAD_REQUEST")
    @Test
    public void givenRestaurantWithInvalidSessionIdWhenAddRestaurantShouldReturnHttp404BadRequest() throws Exception {
        Mockito.when(sessionService.findById(1L)).thenReturn(Optional.empty());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 20L);
        jsonObject.put("restaurantName", "Cafe100");
        jsonObject.put("sessionId", 1L);

        mockMvc.perform(put("/restaurant/addRestaurant").content(jsonObject.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @DisplayName("Given Restaurant with valid sessionId but sessionStatus = CLOSED, when addRestaurant, " +
                 "should return Http 404 BAD_REQUEST")
    @Test
    public void givenRestaurantWithValidSessionIdAndSessionStatusIsClosedWhenAddRestaurantShouldReturnHttp404BadRequest() throws Exception {
        Session s1 = new Session();
        s1.setId(1L);
        s1.setSessionCode("str1Ar4T54");
        s1.setStatus(SessionStatus.CLOSED);
        s1.setInitiatorUserSessionId("fnqwue3h1o8yr012porjnpqfnvoq283yr10iej");
        s1.setStartTime(LocalDateTime.now().minusMinutes(5));

        Mockito.when(sessionService.findById(1L)).thenReturn(Optional.of(s1));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 20L);
        jsonObject.put("restaurantName", "Cafe100");
        jsonObject.put("sessionId", 1L);

        mockMvc.perform(put("/restaurant/addRestaurant").content(jsonObject.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }


}
