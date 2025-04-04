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

    Optional<Webtoon> findByTitleId(Long titleId);

    // 조회수 기준 내림차순 정렬
    Page<Webtoon> findAllByOrderByFavoriteCountDesc(Pageable pageable);

    // 이름으로 검색 (대소문자 구분 없이 부분 일치)
    Page<Webtoon> findByTitleNameContainingIgnoreCase(String titleName, Pageable pageable);

    Page<Webtoon> findByAuthorContaining(String keyword, Pageable pageable);

    @Query("SELECT w FROM Webtoon w JOIN w.tags t WHERE t LIKE %:tag% ORDER BY w.totalCount DESC")
    Page<Webtoon> findByTag(@Param("tag") String tag, Pageable pageable);

}