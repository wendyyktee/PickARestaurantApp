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
import wendy.tee.pickarestaurant.Model.Restaurant;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Repository.RestaurantRepository;
import wendy.tee.pickarestaurant.Repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    RestaurantRepository restaurantRepository;

    @InjectMocks
    RestaurantService restaurantService;

    @Mock
    ResultService resultService;

    @DisplayName("When call method addRestaurant, should create a new Restaurant and return the new Restaurant")
    @Test
    public void givenANewRestaurantShouldSaveARestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(100l);
        restaurant.setSessionId(20l);
        restaurant.setRestaurantName("Danny's");

        Mockito.when(restaurantRepository.save(any())).thenReturn(restaurant);

        Restaurant createdRestaurant = restaurantService.addRestaurant(restaurant);

        assertThat(createdRestaurant).isNotNull();
        assertThat(createdRestaurant.getId()).isEqualTo(100l);
        assertThat(createdRestaurant.getSessionId()).isEqualTo(20l);
        assertThat(createdRestaurant.getRestaurantName()).isEqualTo("Danny's");
    }

    @DisplayName("When call method findBySessionId, should return list of restaurant for the session id")
    @Test
    public void givenSessionIdShouldReturnListOfRestaurantWithCorrespondingSessionId() {
        List<Restaurant> list1 = new ArrayList<>();
        Restaurant r1 = new Restaurant();
        r1.setId(300l);
        r1.setSessionId(20l);
        r1.setRestaurantName("KFF");

        Restaurant r2 = new Restaurant();
        r2.setId(100l);
        r2.setSessionId(20l);
        r2.setRestaurantName("Danny's");

        Restaurant r3 = new Restaurant();
        r3.setId(200l);
        r3.setSessionId(20l);
        r3.setRestaurantName("MCD");
        list1.add(r1);
        list1.add(r2);
        list1.add(r3);

        Mockito.when(restaurantRepository.findBySessionId(20l)).thenReturn(list1);

        List<Restaurant> restaurantList = restaurantService.findBySessionId(20l);

        Assertions.assertThat(restaurantList).hasSize(3);
        Assertions.assertThat(restaurantList).contains(r1, r2, r3);
    }

    @DisplayName("When call method randomPickARestaurantBySessionId, should return a optional Session object.")
    @Test
    public void GivenSessionIdWhenCallRandomPickARestaurantBySessionIdShouldReturnARestaurantFromListOfRestaurants() {
        List<Restaurant> list1 = new ArrayList<>();
        Restaurant r1 = new Restaurant();
        r1.setId(300l);
        r1.setSessionId(20l);
        r1.setRestaurantName("KFF");

        Restaurant r2 = new Restaurant();
        r2.setId(100l);
        r2.setSessionId(20l);
        r2.setRestaurantName("Danny's");

        Restaurant r3 = new Restaurant();
        r3.setId(200l);
        r3.setSessionId(20l);
        r3.setRestaurantName("MCD");
        list1.add(r1);
        list1.add(r2);
        list1.add(r3);

        Mockito.when(restaurantRepository.findBySessionId(20l)).thenReturn(list1);

        Restaurant pickedRestaurant = restaurantService.randomPickARestaurantBySessionId(20l);

        Assertions.assertThat(pickedRestaurant).isNotNull();
        Assertions.assertThat(pickedRestaurant).isIn(list1);
    }
}
