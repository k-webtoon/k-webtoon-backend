package k_webtoons.k_webtoons.repository.webtoon;

import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {

    // 좋아요 수 기준 내림차순 조회
    Page<Webtoon> findAllByOrderByFavoriteCountDesc(Pageable pageable);

    // 제목 키워드 포함 (공개 웹툰만)
    @Query("""
            SELECT w FROM Webtoon w
            WHERE w.isPublic = true
              AND LOWER(w.titleName) LIKE LOWER(CONCAT('%', :titleName, '%'))
            """)
    Page<Webtoon> findByTitleNameContainingIgnoreCase(@Param("titleName") String titleName, Pageable pageable);

    // 작가 키워드 포함 (공개 웹툰만)
    @Query("""
            SELECT w FROM Webtoon w
            WHERE w.isPublic = true
              AND LOWER(w.author) LIKE LOWER(CONCAT('%', :authorName, '%'))
            """)
    Page<Webtoon> findByAuthorContaining(@Param("authorName") String authorName, Pageable pageable);

    // 태그 키워드 포함 (공개 웹툰만)
    @Query("""
            SELECT w FROM Webtoon w
            JOIN w.tags t
            WHERE w.isPublic = true
              AND LOWER(t) LIKE LOWER(CONCAT('%', :tagName, '%'))
            """)
    Page<Webtoon> findByTag(@Param("tagName") String tagName, Pageable pageable);

    // ID 기준으로 제목 조회 (공개 웹툰만)
    @Query("SELECT w.titleName FROM Webtoon w WHERE w.id = :webtoonId AND w.isPublic = true")
    String findTitleById(@Param("webtoonId") Long webtoonId);

    // ID 기준으로 썸네일 URL 조회 (공개 웹툰만)
    @Query("SELECT w.thumbnailUrl FROM Webtoon w WHERE w.id = :webtoonId AND w.isPublic = true")
    String findThumbnailUrlById(@Param("webtoonId") Long webtoonId);

    // ID 기준 상세 조회 (공개 웹툰만)
    @Query("SELECT w FROM Webtoon w WHERE w.id = :id AND w.isPublic = true")
    Optional<Webtoon> findByIdAndIsPublicTrue(@Param("id") Long id);

    // 좋아요 수 기준 Top 웹툰 조회 (공개 웹툰만)
    @Query("SELECT w FROM Webtoon w WHERE w.isPublic = true ORDER BY w.favoriteCount DESC")
    Page<Webtoon> findTopWebtoons(Pageable pageable);

    // 장르 리스트만 조회 (ID 기준)
    @Query("SELECT w.genre FROM Webtoon w WHERE w.id = :id")
    List<String> findGenreByWebtoonId(@Param("id") Long id);

    // 태그 리스트만 조회 (ID 기준)
    @Query("SELECT w.tags FROM Webtoon w WHERE w.id = :id")
    List<String> findTagsByWebtoonId(@Param("id") Long id);

    // OSMU 여부 (osmuOX가 1인 웹툰 개수)
    @Query("SELECT COUNT(w) FROM Webtoon w WHERE w.osmuOX = 1")
    long countOsmuOX();

    // 모든 웹툰을 장르와 함께 가져오기 (genre fetch join)
    @Query("SELECT DISTINCT w FROM Webtoon w LEFT JOIN FETCH w.genre")
    List<Webtoon> findAllWithGenre();

}
