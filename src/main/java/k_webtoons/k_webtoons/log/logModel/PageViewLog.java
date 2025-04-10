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
public class PageViewLog extends BaseLog {

    private String page;

    private Integer duration;

    public PageViewLog(String username, LocalDateTime createdAt, String page, Integer duration) {
        super(username, createdAt);
        this.page = page;
        this.duration = duration;
    }
}
