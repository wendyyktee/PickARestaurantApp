package wendy.tee.pickarestaurant.Repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import wendy.tee.pickarestaurant.Model.Restaurant;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RestaurantRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RestaurantRepository restaurantRepository;

    @DisplayName("Given a new Restaurant object, when call save, should save the Restaurant and return the saved Restaurant")
    @Test
    public void giveNewRestaurantWhenSaveThenSavedSuccess() {
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setRestaurantName("Danny's Cafe");
        newRestaurant.setSessionId(100l);

        Restaurant savedRestaurant = restaurantRepository.save(newRestaurant);

        assertThat(savedRestaurant).hasFieldOrPropertyWithValue("restaurantName", "Danny's Cafe");
        assertThat(savedRestaurant).hasFieldOrPropertyWithValue("sessionId", 100l);
    }

    @DisplayName("Given session Id, when call findBySessionId, should return Session object with given session id.")
    @Test
    public void givenSessionIdWhenFindBySessionIdShouldReturnAllRestaurantWithCorrespondingSessionId() {
        Long sessionId = 300l;
        Restaurant r1 = new Restaurant();
        r1.setRestaurantName("Danny's Cafe");
        r1.setSessionId(sessionId);
        entityManager.persist(r1);

        Restaurant r2 = new Restaurant();
        r2.setRestaurantName("Mac Fried Chicken");
        r2.setSessionId(sessionId);
        entityManager.persist(r2);

        Restaurant r3 = new Restaurant();
        r3.setRestaurantName("Kam Fung Restaurant");
        r3.setSessionId(400l);
        entityManager.persist(r3);

        List<Restaurant> dbRestaurantList = restaurantRepository.findBySessionId(sessionId);

        assertThat(dbRestaurantList).hasSize(2);
        assertThat(dbRestaurantList).contains(r1, r2);
    }
}
