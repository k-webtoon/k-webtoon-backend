package k_webtoons.k_webtoons.repository.webtoon;

import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {

    // 조회수 기준 내림차순 정렬
    @EntityGraph(attributePaths = {"rankGenreTypes"})
    Page<Webtoon> findAllByOrderByFavoriteCountDesc(Pageable pageable);

    // 이름 검색
    @Query("""
            SELECT w FROM Webtoon w
            WHERE w.isPublic = true
            AND LOWER(w.titleName) LIKE LOWER(CONCAT('%', :titleName, '%'))""")
    Page<Webtoon> findByTitleNameContainingIgnoreCase(
            @Param("titleName") String titleName,
            Pageable pageable
    );


    // 작가로 검색
    @Query("""
            SELECT w FROM Webtoon w
            WHERE w.isPublic = true
            AND LOWER(w.author) LIKE LOWER(CONCAT('%', :authorName, '%'))""")
    Page<Webtoon> findByAuthorContaining(
            @Param("authorName") String authorName,
            Pageable pageable
    );

    // 태그로 검색
    @Query("""
            SELECT w FROM Webtoon w
            JOIN w.tags t
            WHERE w.isPublic = true
            AND LOWER(t) LIKE LOWER(CONCAT('%', :tagName, '%'))""")
    Page<Webtoon> findByTag(
            @Param("tagName") String tagName,
            Pageable pageable
    );

    // 웹툰 ID로 제목 조회
    @Query("SELECT w.titleName FROM Webtoon w WHERE w.id = :webtoonId AND w.isPublic = true")
    String findTitleById(@Param("webtoonId") Long webtoonId);

    // 웹툰 ID로 썸네일 URL 조회
    @Query("SELECT w.thumbnailUrl FROM Webtoon w WHERE w.id = :webtoonId AND w.isPublic = true")
    String findThumbnailUrlById(@Param("webtoonId") Long webtoonId);

    // 웹툰 ID로 상세 조회 (연관된 엔티티들을 함께 로딩)
    @Query("SELECT w FROM Webtoon w WHERE w.id = :id AND w.isPublic = true")
    Optional<Webtoon> findByIdAndIsPublicTrue(@Param("id") Long id);

    // 조회수 높은 웹툰 목록 조회 (내림차순 정렬)
    @Query("SELECT w FROM Webtoon w WHERE w.isPublic = true ORDER BY w.favoriteCount DESC")
    Page<Webtoon> findTopWebtoons(Pageable pageable);



}
