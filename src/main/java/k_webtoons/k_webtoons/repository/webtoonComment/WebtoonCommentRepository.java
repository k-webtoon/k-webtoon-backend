package k_webtoons.k_webtoons.repository.webtoonComment;

import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WebtoonCommentRepository extends JpaRepository<WebtoonComment , Long> {

    Optional<WebtoonComment> findByIdAndDeletedDateTimeIsNull(Long id);

    Page<WebtoonComment> findByWebtoonAndDeletedDateTimeIsNull(Webtoon webtoon, Pageable pageable);

    @Query("SELECT wc FROM WebtoonComment wc WHERE wc.appUser.indexId = :userId AND wc.deletedDateTime IS NULL")
    List<WebtoonComment> findByUserIdAndDeletedDateTimeIsNull(@Param("userId") Long userId);
}
