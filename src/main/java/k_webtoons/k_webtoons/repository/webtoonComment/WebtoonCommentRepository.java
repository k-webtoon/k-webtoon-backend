package k_webtoons.k_webtoons.repository.webtoonComment;

import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebtoonCommentRepository extends JpaRepository<WebtoonComment , Long> {

    Optional<WebtoonComment> findByIdAndDeletedDateTimeIsNull(Long id);

    Page<WebtoonComment> findByWebtoonAndDeletedDateTimeIsNull(Webtoon webtoon, Pageable pageable);
}
