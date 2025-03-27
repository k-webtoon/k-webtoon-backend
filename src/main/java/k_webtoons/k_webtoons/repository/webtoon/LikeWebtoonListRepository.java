package k_webtoons.k_webtoons.repository.webtoon;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.LikeWebtoonList;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeWebtoonListRepository extends JpaRepository<LikeWebtoonList, Long> {


    Optional<LikeWebtoonList> findByAppUserAndWebtoon(AppUser appUser, Webtoon webtoon);

    boolean existsByAppUserAndWebtoon(AppUser appUser, Webtoon webtoon);

    @Query("SELECT lwl FROM LikeWebtoonList lwl JOIN FETCH lwl.webtoon WHERE lwl.appUser.indexId = :userId")
    List<LikeWebtoonList> findLikedWebtoonsByUserId(@Param("userId") Long userId);
}
