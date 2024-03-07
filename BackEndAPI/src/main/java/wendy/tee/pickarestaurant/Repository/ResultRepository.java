package wendy.tee.pickarestaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wendy.tee.pickarestaurant.Model.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {
}
