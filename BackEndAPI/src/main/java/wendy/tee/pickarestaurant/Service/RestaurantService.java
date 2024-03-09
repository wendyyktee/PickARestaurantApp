package wendy.tee.pickarestaurant.Service;

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

    public Restaurant addRestaurant(Restaurant restaurant){
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> findBySessionId(Long sessionId){
        return restaurantRepository.findBySessionId(sessionId);
    }

    public Restaurant randomPickARestaurantBySessionId(Long sessionId){
        List<Restaurant> restaurantList = restaurantRepository.findBySessionId(sessionId);

        int randomNumber = getRandomNumber(0, restaurantList.size()-1);
        Restaurant restaurant = restaurantList.get(randomNumber);
        resultService.addResult(sessionId, restaurant.getId(), restaurant.getRestaurantName());

        return restaurant;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
