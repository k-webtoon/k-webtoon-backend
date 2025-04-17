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

public interface WebtoonCommentRepository extends JpaRepository<WebtoonComment, Long> {

    Optional<WebtoonComment> findByIdAndDeletedDateTimeIsNull(Long id);

    @EntityGraph(attributePaths = {"appUser", "likes", "analysis"})
    @Query("SELECT wc FROM WebtoonComment wc WHERE wc.webtoon.id = :webtoonId AND wc.deletedDateTime IS NULL")
    Page<WebtoonComment> findByWebtoonIdAndDeletedDateTimeIsNull(@Param("webtoonId") Long webtoonId, Pageable pageable);

    @Query("SELECT wc FROM WebtoonComment wc WHERE wc.appUser.indexId = :userId AND wc.deletedDateTime IS NULL")
    List<WebtoonComment> findByUserIdAndDeletedDateTimeIsNull(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"appUser"})
    @Query("""
                SELECT
                    wc.id AS commentId,
                    wc.content AS content,
                    au.nickname AS nickname,
                    COUNT(l) AS likeCount
                FROM WebtoonComment wc
                LEFT JOIN wc.likes l
                JOIN wc.appUser au
                WHERE wc.webtoon.id = :webtoonId
                AND wc.deletedDateTime IS NULL
                AND (l IS NULL OR l.isLiked = true)
                GROUP BY wc.id, wc.content, au.nickname
                ORDER BY likeCount DESC
            """)
    List<Object[]> findTop3BestCommentsWithLikeCount(@Param("webtoonId") Long webtoonId, Pageable pageable);

    @EntityGraph(attributePaths = {"appUser"})
    @Query("""
                SELECT wc, COUNT(l) as likeCount
                FROM WebtoonComment wc
                LEFT JOIN wc.likes l
                WHERE wc.webtoon.id = :webtoonId
                AND wc.deletedDateTime IS NULL
                AND l.isLiked = true
                GROUP BY wc.id
                ORDER BY likeCount DESC
            """)
    Page<Object[]> findBestCommentsWithLikeCount(@Param("webtoonId") Long webtoonId, Pageable pageable);

    @Query("""
                SELECT wc
                FROM WebtoonComment wc
                LEFT JOIN FETCH wc.appUser
                WHERE wc.appUser.indexId = :userId
                AND wc.deletedDateTime IS NULL
            """)
    List<WebtoonComment> findUserCommentsWithUser(@Param("userId") Long userId);
    // 삭제된 댓글 수 카운트
    long countByDeletedDateTimeIsNotNull();

}
