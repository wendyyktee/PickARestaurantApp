package wendy.tee.pickarestaurant.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wendy.tee.pickarestaurant.Controller.SessionController;
import wendy.tee.pickarestaurant.Model.Result;
import wendy.tee.pickarestaurant.Repository.ResultRepository;

import java.util.List;

@Service
public class ResultService {

    @Autowired
    ResultRepository resultRepository;

    private Logger logger = LoggerFactory.getLogger(
            ResultService.class);

    public Result addResult(String sessionId, Long restaurantId, String restaurantName){
        Result result = new Result();
        result.setSessionId(sessionId);
        result.setPickedRestaurantId(restaurantId);
        result.setPickedRestaurantName(restaurantName);

        result = resultRepository.save(result);

        logger.debug("New Result created " + result.toString());
        return result;
    }

    public Result findBySessionId(String sessionId){
        List<Result> results = resultRepository.findBySessionId(sessionId);
        logger.info("Total results for sessionId " + sessionId + " is " + results.size());

        if(results.isEmpty()) return null;

        return results.get(0);
    }
}
