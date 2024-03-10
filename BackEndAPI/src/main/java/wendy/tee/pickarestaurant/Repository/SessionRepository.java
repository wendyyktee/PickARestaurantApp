package wendy.tee.pickarestaurant.Repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import wendy.tee.pickarestaurant.Model.Session;
import wendy.tee.pickarestaurant.Enum.SessionStatus;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findBySessionCodeAndStatus(String sessionCode, SessionStatus status);

    List<Session> findBySessionCodeOrderByStartTimeDesc(String sessionCode);
}
