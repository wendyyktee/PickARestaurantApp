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
import wendy.tee.pickarestaurant.Model.Result;
import wendy.tee.pickarestaurant.Repository.ResultRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ResultServiceTest {

    @Mock
    ResultRepository resultRepository;

    @InjectMocks
    ResultService resultService;

    @DisplayName("When call method addResult, should create a new Result record and return the newly created Result")
    @Test
    public void givenANewResultShouldSaveARestaurant() {
        Result result = new Result();
        result.setId(1L);
        result.setSessionId(2000L);
        result.setPickedRestaurantId(3000L);
        result.setPickedRestaurantName("Xiao Fang cafe");

        Mockito.when(resultRepository.save(any())).thenReturn(result);

        Result createdResult = resultService.addResult(2000L, 3000L, "Xiao Fang cafe");

        assertThat(createdResult).isNotNull();
        assertThat(createdResult.getId()).isEqualTo(1L);
        assertThat(createdResult.getSessionId()).isEqualTo(2000L);
        assertThat(createdResult.getPickedRestaurantId()).isEqualTo(3000L);
        assertThat(createdResult.getPickedRestaurantName()).isEqualTo("Xiao Fang cafe");
    }

    @DisplayName("When call method findBySessionId, should return list of restaurant for the session id")
    @Test
    public void givenSessionIdShouldReturnListOfRestaurantWithCorrespondingSessionId() {
        List<Result> list1 = new ArrayList<>();
        Result result = new Result();
        result.setId(1L);
        result.setSessionId(2000L);
        result.setPickedRestaurantId(3000L);
        result.setPickedRestaurantName("Xiao Fang cafe");

        list1.add(result);

        Mockito.when(resultRepository.findBySessionId(2000L)).thenReturn(list1);

        Result queryResult = resultService.findBySessionId(2000L);

        Assertions.assertThat(queryResult).isNotNull();
        assertThat(queryResult).isNotNull();
        assertThat(queryResult.getId()).isEqualTo(1L);
        assertThat(queryResult.getSessionId()).isEqualTo(2000L);
        assertThat(queryResult.getPickedRestaurantId()).isEqualTo(3000L);
        assertThat(queryResult.getPickedRestaurantName()).isEqualTo("Xiao Fang cafe");
    }

}
