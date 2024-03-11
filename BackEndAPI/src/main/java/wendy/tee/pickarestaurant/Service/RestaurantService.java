package wendy.tee.pickarestaurant.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wendy.tee.pickarestaurant.Model.Restaurant;
import wendy.tee.pickarestaurant.Repository.RestaurantRepository;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    ResultService resultService;

    private Logger logger = LoggerFactory.getLogger(
            RestaurantService.class);

    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> findBySessionId(String sessionId) {
        return restaurantRepository.findBySessionId(sessionId);
    }

    public Restaurant randomPickARestaurantBySessionId(String sessionId) {
        List<Restaurant> restaurantList = restaurantRepository.findBySessionId(sessionId);

        if (!restaurantList.isEmpty()) {
            int randomNumber = getRandomNumber(0, restaurantList.size() - 1);
            Restaurant restaurant = restaurantList.get(randomNumber);

            logger.info("Restaurant picked is number " + randomNumber + " from the list, which is " + restaurant.getRestaurantName());
            resultService.addResult(sessionId, restaurant.getId(), restaurant.getRestaurantName());

            return restaurant;
        }
        logger.info("No restaurant under sessionId " + sessionId);
        return null;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
