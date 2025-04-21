package k_webtoons.k_webtoons.service.webtoon;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.UserWebtoonReview;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoon.dto.*;
import k_webtoons.k_webtoons.repository.webtoon.UserWebtoonReviewRepository;
import k_webtoons.k_webtoons.service.auth.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebtoonReviewServiceTest {

    @Mock
    private UserWebtoonReviewRepository reviewRepository;
    @Mock
    private AuthService authService;
    @Mock
    private WebtoonService webtoonService;

    @InjectMocks
    private WebtoonReviewService reviewService;

    private AppUser createUser() {
        AppUser user = new AppUser();
        user.setIndexId(1L);
        return user;
    }

    private Webtoon createWebtoon() {
        Webtoon webtoon = new Webtoon();
        webtoon.setId(1L);
        webtoon.setTitleName("테스트웹툰");
        webtoon.setAuthor("작가");
        webtoon.setThumbnailUrl("thumb.jpg");
        return webtoon;
    }

    @Test
    @DisplayName("좋아요 토글 - 신규 생성 후 좋아요 활성화")
    void 좋아요_토글_신규_생성() {
        // Given
        AppUser user = createUser();
        Webtoon webtoon = createWebtoon();
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(webtoonService.getWebtoonById(1L)).thenReturn(webtoon);
        when(reviewRepository.findByAppUserAndWebtoon(user, webtoon)).thenReturn(Optional.empty());

        // When
        LikeDTO result = reviewService.toggleLike(1L);

        // Then
        assertTrue(result.isLiked());
        verify(reviewRepository).save(any());
    }

    @Test
    @DisplayName("평점 추가 - 유효하지 않은 점수 예외")
    void 평점_유효성_검사() {
        // Given
        RatingRequestDTO invalidRequest = new RatingRequestDTO(6);

        // When & Then
        assertThrows(CustomException.class, () ->
                reviewService.rateWebtoon(1L, invalidRequest));
    }

    @Test
    @DisplayName("평점 추가 - 정상 입력")
    void 평점_정상_입력() {
        // Given
        AppUser user = createUser();
        Webtoon webtoon = createWebtoon();
        RatingRequestDTO request = new RatingRequestDTO(5);

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(webtoonService.getWebtoonById(1L)).thenReturn(webtoon);
        when(reviewRepository.findByAppUserAndWebtoon(user, webtoon)).thenReturn(Optional.empty());

        // When
        RatingDTO result = reviewService.rateWebtoon(1L, request);

        // Then
        assertEquals(1L, result.webtoonId());
        assertEquals(5, result.rating());
    }

    @Test
    @DisplayName("평점 목록 조회 - 성공")
    void 평점_목록_조회_성공() {
        // Given
        AppUser user = createUser();
        Webtoon webtoon = createWebtoon();
        UserWebtoonReview review1 = new UserWebtoonReview(user, webtoon, 4);
        UserWebtoonReview review2 = new UserWebtoonReview(user, webtoon, 5);

        when(authService.getUserByUserId(1L)).thenReturn(user);
        when(reviewRepository.findByAppUserAndRatingIsNotNull(user)).thenReturn(List.of(review1, review2));

        // When
        List<RatingDTO> result = reviewService.getRatings(1L);

        // Then
        assertEquals(2, result.size());
        assertEquals(4, result.get(0).rating());
    }

    @Test
    @DisplayName("즐겨찾기 토글 - 기존 값 null에서 true로 변경")
    void 즐겨찾기_토글_처음_토글() {
        // Given
        AppUser user = createUser();
        Webtoon webtoon = createWebtoon();
        UserWebtoonReview review = new UserWebtoonReview();
        review.setIsFavorite(null);

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(webtoonService.getWebtoonById(1L)).thenReturn(webtoon);
        when(reviewRepository.findByAppUserAndWebtoon(user, webtoon)).thenReturn(Optional.of(review));

        // When
        FavoriteDTO result = reviewService.toggleFavorite(1L);

        // Then
        assertTrue(result.isFavorite());
    }

    @Test
    @DisplayName("봤어요 목록 조회 - 결과 없을 때 빈 리스트 반환")
    void 봤어요_목록_조회_결과없음() {
        // Given
        when(authService.getUserByUserId(1L)).thenReturn(createUser());
        when(reviewRepository.findByAppUserAndIsWatchedTrue(any())).thenReturn(List.of());

        // When
        List<WatchedDTO> result = reviewService.getWatchedList(1L);

        // Then
        assertTrue(result.isEmpty());
    }
}
