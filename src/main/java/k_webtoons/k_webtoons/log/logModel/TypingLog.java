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
public class TypingLog extends BaseLog {
    private String keyword;
    private String source;

    public TypingLog(String username, LocalDateTime createdAt, String keyword, String source) {
        super(username, createdAt);
        this.keyword = keyword;
        this.source = source;
    }
}