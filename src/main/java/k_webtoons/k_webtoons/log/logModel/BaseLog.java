package k_webtoons.k_webtoons.log.logModel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @CreatedDate
    private LocalDateTime createdAt;

    public BaseLog(String username, LocalDateTime createdAt) {
        this.username = username;
        this.createdAt = createdAt;
    }
}
