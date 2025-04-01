package k_webtoons.k_webtoons.model.webtoonComment;

import jakarta.persistence.*;
import k_webtoons.k_webtoons.model.auth.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "comment_like", uniqueConstraints = {@UniqueConstraint(name = "unique_user_comment", columnNames = {"user_id", "comment_id"})})
public class CommentLike {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private AppUser appUser;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "comment_id" , nullable = false)
        private WebtoonComment webtoonComment;

        @Column(nullable = false)
        private LocalDateTime likedAt; // 좋아요 누른 시간

        @Column(nullable = false)
        private boolean isLiked;

}
