package wendy.tee.pickarestaurant.Service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    private String sessionId;
    private Result result;

    @BeforeEach
    public void setup() {
        sessionId = "s9n10gm7PXKn";
        result = Result
                .builder()
                .id(111L)
                .sessionId(sessionId)
                .pickedRestaurantId(222L)
                .pickedRestaurantName("KFFF")
                .build();
    }

    @DisplayName("When call method addResult, should create a new Result record and return the newly created Result")
    @Test
    public void givenANewResultShouldSaveARestaurant() {
        Mockito.when(resultRepository.save(any())).thenReturn(result);

        Result createdResult = resultService.addResult(sessionId, 222L, "KFFF");

        assertThat(createdResult).isNotNull();
        assertThat(createdResult.getId()).isEqualTo(111L);
        assertThat(createdResult.getSessionId()).isEqualTo(sessionId);
        assertThat(createdResult.getPickedRestaurantId()).isEqualTo(222L);
        assertThat(createdResult.getPickedRestaurantName()).isEqualTo("KFFF");
    }

    @DisplayName("When call method findBySessionId, should return list of restaurant for the session id")
    @Test
    public void givenSessionIdShouldReturnListOfRestaurantWithCorrespondingSessionId() {
        List<Result> resultList = new ArrayList<>();
        resultList.add(result);

        Mockito.when(resultRepository.findBySessionId(sessionId)).thenReturn(resultList);

        Result queryResult = resultService.findBySessionId(sessionId);

        Assertions.assertThat(queryResult).isNotNull();
        assertThat(queryResult).isNotNull();
        assertThat(queryResult.getId()).isEqualTo(111L);
        assertThat(queryResult.getSessionId()).isEqualTo(sessionId);
        assertThat(queryResult.getPickedRestaurantId()).isEqualTo(222L);
        assertThat(queryResult.getPickedRestaurantName()).isEqualTo("KFFF");
    }
}
