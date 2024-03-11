package wendy.tee.pickarestaurant.Repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import wendy.tee.pickarestaurant.Model.Restaurant;
import wendy.tee.pickarestaurant.Model.Result;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ResultRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ResultRepository resultRepository;

    @DisplayName("Given a new Result, when Save, should save the given Result and return the saved Result")
    @Test
    public void giveNewResultWhenSaveThenSavedSuccess() {
        Result result = new Result();
        result.setSessionId("s9n10gm7PXKn");
        result.setPickedRestaurantId(222L);
        result.setPickedRestaurantName("Danny's Cafe");

        Result savedResult = resultRepository.save(result);

        assertThat(savedResult).hasFieldOrProperty("id").isNotNull();
        assertThat(savedResult).hasFieldOrPropertyWithValue("sessionId", "s9n10gm7PXKn");
        assertThat(savedResult).hasFieldOrPropertyWithValue("pickedRestaurantId", 222L);
        assertThat(savedResult).hasFieldOrPropertyWithValue("pickedRestaurantName", "Danny's Cafe");
    }

    @DisplayName("Given session Id, when call findBySessionId, should return Session with given session id.")
    @Test
    public void givenSessionIdWhenFindBySessionIdShouldReturnAllRestaurantWithCorrespondingSessionId() {
        String sessionId = "s9n10gm7PXKn";

        Result result = new Result();
        result.setSessionId(sessionId);
        result.setPickedRestaurantId(222L);
        result.setPickedRestaurantName("Danny's Cafe");
        entityManager.persist(result);

        List<Result> resultList = resultRepository.findBySessionId(sessionId);

        assertThat(resultList).hasSize(1);
        assertThat(resultList).contains(result);
    }
}
