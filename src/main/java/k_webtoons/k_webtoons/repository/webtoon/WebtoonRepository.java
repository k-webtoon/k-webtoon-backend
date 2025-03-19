package k_webtoons.k_webtoons.repository.webtoon;

import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {

    Optional<Webtoon> findByTitleId(Long titleId);
}
