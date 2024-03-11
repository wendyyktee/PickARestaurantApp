package wendy.tee.pickarestaurant.Repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Enum.SessionStatus;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {
}
