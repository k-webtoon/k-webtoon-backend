package k_webtoons.k_webtoons.controller.webtoon;

import k_webtoons.k_webtoons.model.webtoon.dto.*;
import k_webtoons.k_webtoons.service.webtoon.WebtoonReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebtoonReviewControllerTest {

    @Mock
    private WebtoonReviewService reviewService;

    @InjectMocks
    private WebtoonReviewController reviewController;

    @Test
    @DisplayName("좋아요 토글 - 성공")
    void 좋아요_토글_성공() {
        // Given
        LikeDTO mockResponse = new LikeDTO(1L, true);
        when(reviewService.toggleLike(1L)).thenReturn(mockResponse);

        // When
        ResponseEntity<LikeDTO> response = reviewController.toggleLike(1L);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isLiked());
    }

    @Test
    @DisplayName("평점 추가 - 성공")
    void 평점_추가_성공() {
        // Given
        RatingDTO mockResponse = new RatingDTO(1L, 4);
        RatingRequestDTO request = new RatingRequestDTO(4);
        when(reviewService.rateWebtoon(1L, request)).thenReturn(mockResponse);

        // When
        ResponseEntity<RatingDTO> response = reviewController.rateWebtoon(1L, request);

        // Then
        assertEquals(4, response.getBody().rating());
        assertEquals(1L, response.getBody().webtoonId());
    }

    @Test
    @DisplayName("평점 목록 조회 - 성공")
    void 평점_목록_조회_성공() {
        // Given
        List<RatingDTO> mockList = List.of(new RatingDTO(1L, 5), new RatingDTO(2L, 3));
        when(reviewService.getRatings(1L)).thenReturn(mockList);

        // When
        ResponseEntity<List<RatingDTO>> response = reviewController.getRatings(1L);

        // Then
        assertEquals(2, response.getBody().size());
        assertEquals(5, response.getBody().get(0).rating());
    }

    @Test
    @DisplayName("즐겨찾기 토글 - 성공")
    void 즐겨찾기_토글_성공() {
        // Given
        FavoriteDTO mockResponse = new FavoriteDTO(1L, true);
        when(reviewService.toggleFavorite(1L)).thenReturn(mockResponse);

        // When
        ResponseEntity<FavoriteDTO> response = reviewController.toggleFavorite(1L);

        // Then
        assertTrue(response.getBody().isFavorite());
    }

    @Test
    @DisplayName("봤어요 토글 - 성공")
    void 봤어요_토글_성공() {
        // Given
        WatchedDTO mockResponse = new WatchedDTO(1L, true);
        when(reviewService.toggleWatched(1L)).thenReturn(mockResponse);

        // When
        ResponseEntity<WatchedDTO> response = reviewController.toggleWatched(1L);

        // Then
        assertTrue(response.getBody().isWatched());
    }

    @Test
    @DisplayName("좋아요 목록 조회 - 성공")
    void 좋아요_목록_조회_성공() {
        // Given
        List<LikeReloadDTO> mockList = List.of(
                new LikeReloadDTO(1L, true, "웹툰1", "작가1", "thumb1.jpg"),
                new LikeReloadDTO(2L, true, "웹툰2", "작가2", "thumb2.jpg")
        );
        when(reviewService.getLikes(1L)).thenReturn(mockList);

        // When
        ResponseEntity<List<LikeReloadDTO>> response = reviewController.getLikes(1L);

        // Then
        assertEquals(2, response.getBody().size());
        assertEquals("웹툰1", response.getBody().get(0).titleName());
    }

    @Test
    @DisplayName("즐겨찾기 목록 조회 - 성공")
    void 즐겨찾기_목록_조회_성공() {
        // Given
        List<FavoriteDTO> mockList = List.of(new FavoriteDTO(1L, true));
        when(reviewService.getFavorites(1L)).thenReturn(mockList);

        // When
        ResponseEntity<List<FavoriteDTO>> response = reviewController.getFavorites(1L);

        // Then
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).isFavorite());
    }

    @Test
    @DisplayName("봤어요 목록 조회 - 성공")
    void 봤어요_목록_조회_성공() {
        // Given
        List<WatchedDTO> mockList = List.of(new WatchedDTO(1L, true));
        when(reviewService.getWatchedList(1L)).thenReturn(mockList);

        // When
        ResponseEntity<List<WatchedDTO>> response = reviewController.getWatchedList(1L);

        // Then
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).isWatched());
    }
}