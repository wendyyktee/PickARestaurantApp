package wendy.tee.pickarestaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wendy.tee.pickarestaurant.Model.Restaurant;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findBySessionId(String sessionId);
}
