package wendy.tee.pickarestaurant.Model;

import lombok.Getter;
import lombok.Setter;
import wendy.tee.pickarestaurant.Enum.SessionStatus;

@Getter
@Setter
public class SessionResponse {

    private Long id;
    private String sessionCode;
    private SessionStatus status;
    private String initiator;
}
