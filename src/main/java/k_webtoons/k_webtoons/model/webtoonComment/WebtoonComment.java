package k_webtoons.k_webtoons.model.webtoonComment;

import jakarta.persistence.*;
import k_webtoons.k_webtoons.model.user.AppUser;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "webtoon_comment")
public class WebtoonComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(nullable = true)
    private LocalDateTime deletedDateTime; // 댓글 삭제 시간 (삭제된 경우 해당 시간 기록)

    @OneToMany(mappedBy = "webtoonComment", cascade = CascadeType.ALL , orphanRemoval = true)
    @Builder.Default
    private List<CommentLike> likes = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }

    // 댓글이 삭제된 상태인지 확인하는 메서드
    public boolean isDeleted() {
        return deletedDateTime != null;
    }

    // 댓글을 삭제 처리하는 메서드
    public void deleteComment() {
        this.deletedDateTime = LocalDateTime.now(); // 삭제 시 삭제 시간 기록
    }

}
