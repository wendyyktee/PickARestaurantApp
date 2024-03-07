package wendy.tee.pickarestaurant.Repository;

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

    @Test
    public void giveNewRestaurantWhenSaveThenSavedSuccess() {
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setRestaurantName("Danny's Cafe");
        newRestaurant.setVoterName("Wendy Tee");
        newRestaurant.setSessionId(100l);

        Restaurant savedRestaurant = restaurantRepository.save(newRestaurant);

        assertThat(savedRestaurant).hasFieldOrPropertyWithValue("restaurantName", "Danny's Cafe");
        assertThat(savedRestaurant).hasFieldOrPropertyWithValue("voterName", "Wendy Tee");
        assertThat(savedRestaurant).hasFieldOrPropertyWithValue("sessionId", 100l);
    }

    @Test
    public void givenSessionIdWhenFindBySessionIdShouldReturnAllRestaurantWithCorrespondingSessionId() {
        Long sessionId = 300l;
        Restaurant r1 = new Restaurant();
        r1.setRestaurantName("Danny's Cafe");
        r1.setVoterName("Wendy Tee");
        r1.setSessionId(sessionId);
        entityManager.persist(r1);

        Restaurant r2 = new Restaurant();
        r2.setRestaurantName("Mac Fried Chicken");
        r2.setVoterName("Cai");
        r2.setSessionId(sessionId);
        entityManager.persist(r2);

        Restaurant r3 = new Restaurant();
        r3.setRestaurantName("Kam Fung Restaurant");
        r3.setVoterName("Gaya");
        r3.setSessionId(400l);
        entityManager.persist(r3);

        List<Restaurant> dbRestaurantList = restaurantRepository.findBySessionId(sessionId);

        assertThat(dbRestaurantList).hasSize(2);
        assertThat(dbRestaurantList).contains(r1, r2);
    }
}
