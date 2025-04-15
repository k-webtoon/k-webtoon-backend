package k_webtoons.k_webtoons.repository.webtoon;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.UserWebtoonReview;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserWebtoonReviewRepository extends JpaRepository<UserWebtoonReview, Long> {

    boolean existsByAppUserAndWebtoon(AppUser appUser, Webtoon webtoon);

    @Query("SELECT uwr FROM UserWebtoonReview uwr JOIN FETCH uwr.webtoon WHERE uwr.appUser.indexId = :userIndexId")
    List<UserWebtoonReview> findLikedWebtoonsByUserId(@Param("userId") Long userId);

    List<UserWebtoonReview> findByAppUser(AppUser appUser);

    // 사용자와 웹툰으로 리뷰 조회
    Optional<UserWebtoonReview> findByAppUserAndWebtoon(AppUser user, Webtoon webtoon);

    // 좋아요 목록 조회
    List<UserWebtoonReview> findByAppUserAndIsLikedTrue(AppUser user);

    // 평점 목록 조회
    List<UserWebtoonReview> findByAppUserAndRatingIsNotNull(AppUser user);

    // 즐겨찾기 목록 조회
    List<UserWebtoonReview> findByAppUserAndIsFavoriteTrue(AppUser user);

    // 봤어요 목록 조회
    List<UserWebtoonReview> findByAppUserAndIsWatchedTrue(AppUser user);

    // 사용자의 좋아요 또는 즐겨찾기 웹툰 조회
    @Query("SELECT r FROM UserWebtoonReview r WHERE r.appUser = :user AND (r.isLiked = true OR r.isFavorite = true)")
    List<UserWebtoonReview> findUserLikedOrFavoritedWebtoons(@Param("user") AppUser user);
    
    // 즐겨찾기가 많은 순으로 웹툰 조회
    @Query("SELECT uwr.webtoon.id AS webtoonId, COUNT(uwr) AS favoriteCount " +
           "FROM UserWebtoonReview uwr " +
           "WHERE uwr.isFavorite = true " +
           "GROUP BY uwr.webtoon.id " +
           "ORDER BY COUNT(uwr) DESC")
    List<Object[]> findMostFavoritedWebtoons(Pageable pageable);
    
    // 좋아요가 많은 순으로 웹툰 조회
    @Query("SELECT uwr.webtoon.id AS webtoonId, COUNT(uwr) AS likeCount " +
           "FROM UserWebtoonReview uwr " +
           "WHERE uwr.isLiked = true " +
           "GROUP BY uwr.webtoon.id " +
           "ORDER BY COUNT(uwr) DESC")
    List<Object[]> findMostLikedWebtoons(Pageable pageable);
    
    // 봤어요가 많은 순으로 웹툰 조회
    @Query("SELECT uwr.webtoon.id AS webtoonId, COUNT(uwr) AS watchedCount " +
           "FROM UserWebtoonReview uwr " +
           "WHERE uwr.isWatched = true " +
           "GROUP BY uwr.webtoon.id " +
           "ORDER BY COUNT(uwr) DESC")
    List<Object[]> findMostWatchedWebtoons(Pageable pageable);
}

