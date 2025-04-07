package k_webtoons.k_webtoons.repository.webtoon;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.RecommendWebtoon;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendWebtoonRepository extends JpaRepository<RecommendWebtoon, Long> {
    Optional<RecommendWebtoon> findByAppUserAndWebtoon(AppUser appUser, Webtoon webtoon);
}
