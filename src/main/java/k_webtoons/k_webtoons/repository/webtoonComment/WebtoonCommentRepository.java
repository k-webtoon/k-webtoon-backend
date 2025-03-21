package k_webtoons.k_webtoons.repository.webtoonComment;

import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebtoonCommentRepository extends JpaRepository<WebtoonComment , Long> {

    Optional<WebtoonComment> findByIdAndDeletedDateTimeIsNull(Long id);
}
