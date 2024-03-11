package wendy.tee.pickarestaurant.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import wendy.tee.pickarestaurant.Enum.SessionStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@ToString
public class Session {

    @Id
    @Column(length = 12)
    private String id;
    private SessionStatus status;
    private String initiatorUserSessionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
