package k_webtoons.k_webtoons.repository.webtoonComment;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoonComment.CommentLike;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike , Long> {

    // 특정 사용자와 댓글 간의 좋아요 여부 확인
    Optional<CommentLike> findByAppUserAndWebtoonComment(AppUser appUser, WebtoonComment webtoonComment);

    long countByWebtoonCommentAndIsLikedTrue(WebtoonComment webtoonComment); // 좋아요 상태가 true인 개수
}
