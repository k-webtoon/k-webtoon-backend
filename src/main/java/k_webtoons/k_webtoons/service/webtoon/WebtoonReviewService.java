package k_webtoons.k_webtoons.service.webtoon;


import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.dto.*;
import k_webtoons.k_webtoons.model.webtoon.UserWebtoonReview;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.repository.webtoon.UserWebtoonReviewRepository;
import k_webtoons.k_webtoons.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebtoonReviewService {

    private final UserWebtoonReviewRepository reviewRepository;
    private final AuthService authService;
    private final WebtoonService webtoonService;

    // 좋아요 토글 (true → false → null 순환)
    @Transactional
    public LikeDTO toggleLike(Long webtoonId) {
        AppUser user = authService.getAuthenticatedUser();
        Webtoon webtoon = webtoonService.getWebtoonById(webtoonId);

        UserWebtoonReview review = reviewRepository.findByAppUserAndWebtoon(user, webtoon)
                .orElseGet(() -> UserWebtoonReview.builder()
                        .appUser(user)
                        .webtoon(webtoon)
                        .build());

        // 상태 순환 로직
        if (review.getIsLiked() == null) {
            review.setIsLiked(true);
        } else if (review.getIsLiked()) {
            review.setIsLiked(false);
        } else {
            review.setIsLiked(null);
        }

        reviewRepository.save(review);
        return new LikeDTO(webtoon.getId(), review.getIsLiked());
    }

    // 평점 추가/수정
    @Transactional
    public RatingDTO rateWebtoon(Long webtoonId, RatingRequestDTO request) {
        if (request.rating() < 1 || request.rating() > 5) {
            throw new CustomException("평점은 1~5점만 가능합니다.", "INVALID_RATING");
        }

        AppUser user = authService.getAuthenticatedUser();
        Webtoon webtoon = webtoonService.getWebtoonById(webtoonId);

        UserWebtoonReview review = reviewRepository.findByAppUserAndWebtoon(user, webtoon)
                .orElseGet(() -> new UserWebtoonReview(user, webtoon, request.rating()));

        review.setRating(request.rating());
        reviewRepository.save(review);

        return new RatingDTO(webtoon.getId(), review.getRating());
    }

    // 즐겨찾기 토글
    @Transactional
    public FavoriteDTO toggleFavorite(Long webtoonId) {
        AppUser user = authService.getAuthenticatedUser();
        Webtoon webtoon = webtoonService.getWebtoonById(webtoonId);

        UserWebtoonReview review = reviewRepository.findByAppUserAndWebtoon(user, webtoon)
                .orElseGet(() -> {
                    UserWebtoonReview newReview = new UserWebtoonReview(user, webtoon, false);
                    return newReview;
                });

        // null-safe 토글 처리
        Boolean current = review.getIsFavorite();
        if (current == null) {
            review.setIsFavorite(true);
        } else {
            review.setIsFavorite(!current);
        }

        reviewRepository.save(review);

        return new FavoriteDTO(webtoon.getId(), review.getIsFavorite());
    }

    // 봤어요 토글
    @Transactional
    public WatchedDTO toggleWatched(Long webtoonId) {
        AppUser user = authService.getAuthenticatedUser();
        Webtoon webtoon = webtoonService.getWebtoonById(webtoonId);

        UserWebtoonReview review = reviewRepository.findByAppUserAndWebtoon(user, webtoon)
                .orElseGet(() -> UserWebtoonReview.builder()
                        .appUser(user)
                        .webtoon(webtoon)
                        .build());

        // 상태 순환 로직
        if (review.getIsWatched() == null) {
            review.setIsWatched(true);
        } else if (review.getIsWatched()) {
            review.setIsWatched(false);
        } else {
            review.setIsWatched(null);
        }

        reviewRepository.save(review);
        return new WatchedDTO(webtoon.getId(), review.getIsWatched());
    }

    // 사용자별 좋아요 목록 조회 (모든 리뷰 반환)
    @Transactional(readOnly = true)
    public List<LikeReloadDTO> getLikes(Long userId) {
        AppUser user = authService.getUserByUserId(userId);

        return reviewRepository.findByAppUserAndIsLikedTrue(user).stream()
                .map(review -> {
                    Webtoon webtoon = review.getWebtoon();
                    return new LikeReloadDTO(
                            webtoon.getId(),
                            true,
                            webtoon.getTitleName(),
                            webtoon.getAuthor(),
                            webtoon.getThumbnailUrl()
                    );
                })
                .collect(Collectors.toList());
    }



    // 사용자별 평점 목록 조회
    @Transactional(readOnly = true)
    public List<RatingDTO> getRatings(Long userId) {
        AppUser user = authService.getUserByUserId(userId);
        return reviewRepository.findByAppUserAndRatingIsNotNull(user).stream()
                .map(review -> new RatingDTO(review.getWebtoon().getId(), review.getRating()))
                .collect(Collectors.toList());
    }

    // 사용자별 즐겨찾기 목록 조회
    @Transactional(readOnly = true)
    public List<FavoriteDTO> getFavorites(Long userId) {
        AppUser user = authService.getUserByUserId(userId);
        return reviewRepository.findByAppUserAndIsFavoriteTrue(user).stream()
                .map(review -> new FavoriteDTO(review.getWebtoon().getId(), true))
                .collect(Collectors.toList());
    }

    // 사용자별 봤어요 목록 조회
    @Transactional(readOnly = true)
    public List<WatchedDTO> getWatchedList(Long userId) {
        AppUser user = authService.getUserByUserId(userId);
        return reviewRepository.findByAppUserAndIsWatchedTrue(user).stream()
                .map(review -> new WatchedDTO(review.getWebtoon().getId(), true))
                .collect(Collectors.toList());
    }
}

