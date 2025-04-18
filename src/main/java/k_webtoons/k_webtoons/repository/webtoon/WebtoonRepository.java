package k_webtoons.k_webtoons.repository.webtoon;

import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {

    @EntityGraph(attributePaths = {"rankGenreTypes", "tags"})
    Page<Webtoon> findAllByOrderByFavoriteCountDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"tags"})
    @Query("""
            SELECT w FROM Webtoon w
            WHERE w.isPublic = true
              AND LOWER(w.titleName) LIKE LOWER(CONCAT('%', :titleName, '%'))
            """)
    Page<Webtoon> findByTitleNameContainingIgnoreCase(@Param("titleName") String titleName, Pageable pageable);

    @EntityGraph(attributePaths = {"tags"})
    @Query("""
            SELECT w FROM Webtoon w
            WHERE w.isPublic = true
              AND LOWER(w.author) LIKE LOWER(CONCAT('%', :authorName, '%'))
            """)
    Page<Webtoon> findByAuthorContaining(@Param("authorName") String authorName, Pageable pageable);

    @EntityGraph(attributePaths = {"tags"})
    @Query("""
            SELECT w FROM Webtoon w
            JOIN w.tags t
            WHERE w.isPublic = true
              AND LOWER(t) LIKE LOWER(CONCAT('%', :tagName, '%'))
            """)
    Page<Webtoon> findByTag(@Param("tagName") String tagName, Pageable pageable);

    @Query("SELECT w.titleName FROM Webtoon w WHERE w.id = :webtoonId AND w.isPublic = true")
    String findTitleById(@Param("webtoonId") Long webtoonId);

    @Query("SELECT w.thumbnailUrl FROM Webtoon w WHERE w.id = :webtoonId AND w.isPublic = true")
    String findThumbnailUrlById(@Param("webtoonId") Long webtoonId);

    @EntityGraph(attributePaths = {"tags"})
    @Query("SELECT w FROM Webtoon w WHERE w.id = :id AND w.isPublic = true")
    Optional<Webtoon> findByIdAndIsPublicTrue(@Param("id") Long id);

    @EntityGraph(attributePaths = {"tags"})
    @Query("SELECT w FROM Webtoon w WHERE w.isPublic = true ORDER BY w.favoriteCount DESC")
    Page<Webtoon> findTopWebtoons(Pageable pageable);

    @Query("SELECT w.genre FROM Webtoon w WHERE w.id = :id")
    List<String> findGenreByWebtoonId(@Param("id") Long id);

    @Query("SELECT w.tags FROM Webtoon w WHERE w.id = :id")
    List<String> findTagsByWebtoonId(@Param("id") Long id);

    @Query("SELECT COUNT(w) FROM Webtoon w WHERE w.osmuOX = 1")
    long countOsmuOX();

    @Query("SELECT DISTINCT w FROM Webtoon w LEFT JOIN FETCH w.genre")
    List<Webtoon> findAllWithGenre();

    @EntityGraph(attributePaths = {"tags"})
    @Query("SELECT w FROM Webtoon w WHERE w.isPublic = true")
    Page<Webtoon> findPublicWebtoons(Pageable pageable);

    @EntityGraph(attributePaths = {"tags"})
    @Query("SELECT w FROM Webtoon w WHERE w.isPublic = false")
    Page<Webtoon> findPrivateWebtoons(Pageable pageable);

    @EntityGraph(attributePaths = {"tags"})
    @Query("""
                SELECT w FROM Webtoon w
                WHERE LOWER(w.titleName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(w.author) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Webtoon> findByTitleOrAuthorContaining(@Param("keyword") String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"tags"})
    @Query("""
                SELECT w FROM Webtoon w
                WHERE w.isPublic = true AND
                      (LOWER(w.titleName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                       LOWER(w.author) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Webtoon> findPublicWebtoonsByTitleOrAuthor(@Param("keyword") String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"tags"})
    @Query("""
                SELECT w FROM Webtoon w
                WHERE w.isPublic = false AND
                      (LOWER(w.titleName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                       LOWER(w.author) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Webtoon> findPrivateWebtoonsByTitleOrAuthor(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(w) FROM Webtoon w WHERE w.isPublic = true")
    long countPublicWebtoons();

    @Query("SELECT COUNT(w) FROM Webtoon w WHERE w.isPublic = false")
    long countPrivateWebtoons();
}
