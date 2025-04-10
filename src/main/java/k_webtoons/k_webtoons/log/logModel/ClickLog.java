package k_webtoons.k_webtoons.log.logModel;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ClickLog extends BaseLog {
    private String page;
    private String target;

    public ClickLog(String username, LocalDateTime createdAt, String page, String target) {
        super(username, createdAt);
        this.page = page;
        this.target = target;
    }


}
