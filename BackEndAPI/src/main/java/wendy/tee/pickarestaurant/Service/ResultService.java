package wendy.tee.pickarestaurant.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wendy.tee.pickarestaurant.Model.Result;
import wendy.tee.pickarestaurant.Repository.ResultRepository;

import java.util.List;

@Service
public class ResultService {

    @Autowired
    ResultRepository resultRepository;

    public Result addResult(Long sessionId, Long restaurantId, String restaurantName){
        Result result = new Result();
        result.setSessionId(sessionId);
        result.setPickedRestaurantId(restaurantId);
        result.setPickedRestaurantName(restaurantName);
        return resultRepository.save(result);
    }

    public Result findBySessionId(Long sessionId){
        List<Result> results = resultRepository.findBySessionId(sessionId);

        if(results.isEmpty()) return null;

        return results.get(0);
    }
}
