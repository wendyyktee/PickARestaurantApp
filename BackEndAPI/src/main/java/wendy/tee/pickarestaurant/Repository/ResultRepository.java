package wendy.tee.pickarestaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wendy.tee.pickarestaurant.Model.Result;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findBySessionId(String sessionId);
}
