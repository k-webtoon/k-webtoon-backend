package k_webtoons.k_webtoons.repository.webtoonComment;

import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WebtoonCommentRepository extends JpaRepository<WebtoonComment , Long> {

    Optional<WebtoonComment> findByIdAndDeletedDateTimeIsNull(Long id);

    @EntityGraph(attributePaths = {"appUser", "webtoon"})
    @Query("SELECT wc FROM WebtoonComment wc WHERE wc.webtoon.id = :webtoonId AND wc.deletedDateTime IS NULL")
    Page<WebtoonComment> findByWebtoonIdAndDeletedDateTimeIsNull(@Param("webtoonId") Long webtoonId, Pageable pageable);

    @Query("SELECT wc FROM WebtoonComment wc WHERE wc.appUser.indexId = :userId AND wc.deletedDateTime IS NULL")
    List<WebtoonComment> findByUserIdAndDeletedDateTimeIsNull(@Param("userId") Long userId);

    @Query(value = """
    SELECT c.* FROM webtoon_comment c
    LEFT JOIN comment_like l ON c.id = l.comment_id AND l.is_liked = true
    WHERE c.webtoon_id = :webtoonId
      AND c.deleted_date_time IS NULL
    GROUP BY c.id
    ORDER BY COUNT(l.id) DESC
    LIMIT 3
    """, nativeQuery = true)
    List<WebtoonComment> findTop3BestComments(@Param("webtoonId") Long webtoonId);
}
