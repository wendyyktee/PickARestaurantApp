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

    public Restaurant addRestaurant(Long sessionId, String restaurantName, String voterName){
        Restaurant restaurant = new Restaurant();
        restaurant.setSessionId(sessionId);
        restaurant.setRestaurantName(restaurantName);
        restaurant.setVoterName(voterName);
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> findBySessionId(Long sessionId){
        return restaurantRepository.findBySessionId(sessionId);
    }
}
