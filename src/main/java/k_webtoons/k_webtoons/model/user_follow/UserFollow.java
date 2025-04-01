package k_webtoons.k_webtoons.model.user_follow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import k_webtoons.k_webtoons.model.auth.AppUser;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 사용자가 다른 사용자를 팔로우할 때의 관계를 표현하는 엔티티 클래스입니다.
 * follower가 followee를 팔로우합니다.
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(
        name = "user_follow",
        uniqueConstraints = {
                // 동일한 사용자가 같은 사용자를 중복 팔로우하는 것을 방지
                @UniqueConstraint(columnNames = {"follower_id", "followee_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFollow {

    /**
     * 사용자 팔로우 관계의 고유 식별자 (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 팔로우를 하는 사용자 (팔로워)
     * Lazy 로딩을 통해 필요한 경우에만 데이터를 가져옵니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private AppUser follower;

    /**
     * 팔로우를 받는 사용자 (팔로이)
     * Lazy 로딩을 통해 필요한 경우에만 데이터를 가져옵니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id", nullable = false)
    private AppUser followee;

    /**
     * 팔로우가 발생한 시각 (생성 시점 자동 설정)
     * 최초 생성 시에만 설정되며 이후 수정되지 않습니다.
     */
    @Column(name = "followed_at", nullable = false, updatable = false)
    private LocalDateTime followedAt = LocalDateTime.now();
}
