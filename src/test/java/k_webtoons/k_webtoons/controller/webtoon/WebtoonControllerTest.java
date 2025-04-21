package k_webtoons.k_webtoons.controller.webtoon;

import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonDetailResponse;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonViewCountResponse;
import k_webtoons.k_webtoons.service.webtoon.WebtoonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebtoonControllerTest {

    @Mock
    private WebtoonService webtoonService;

    @InjectMocks
    private WebtoonController webtoonController;

    private WebtoonViewCountResponse createDummyWebtoon() {
        return new WebtoonViewCountResponse(
                1L, 1001L, "테스트 웹툰", "작가명",
                false, "15", false, "thumb.jpg",
                "시놉시스", List.of("코믹"), 4.5, 1000L
        );
    }

    private WebtoonDetailResponse createDummyDetail() {
        return new WebtoonDetailResponse(
                1L, "테스트 웹툰", "작가명", "thumb.jpg",
                "시놉시스", "15", "4.50",
                false, false, false, false, false, false,
                false, false,
                List.of("코믹"), List.of("태그1"), "ART001", "external.url"
        );
    }

    @Test
    @DisplayName("조회수 높은 웹툰 리스트 조회 - 성공")
    void getTopWebtoons() {
        // Given
        Page<WebtoonViewCountResponse> page = new PageImpl<>(List.of(createDummyWebtoon()));
        when(webtoonService.getTopWebtoons(anyInt(), anyInt())).thenReturn(page);

        // When
        ResponseEntity<Page<WebtoonViewCountResponse>> response = webtoonController.getTopWebtoons(0, 10);

        // Then
        assertEquals(1, response.getBody().getContent().size());
        assertEquals("테스트 웹툰", response.getBody().getContent().get(0).titleName());
    }

    @Test
    @DisplayName("웹툰 이름 검색 - 성공")
    void searchWebtoonsByName() {
        // Given
        Page<WebtoonViewCountResponse> page = new PageImpl<>(List.of(createDummyWebtoon()));
        when(webtoonService.searchWebtoonsByName(anyString(), anyInt(), anyInt())).thenReturn(page);

        // When
        ResponseEntity<Page<WebtoonViewCountResponse>> response = webtoonController.searchWebtoonsByName("테스트", 0, 10);

        // Then
        assertEquals(1, response.getBody().getContent().size());
        assertEquals("테스트 웹툰", response.getBody().getContent().get(0).titleName());
    }

    @Test
    @DisplayName("작가명 검색 - 성공")
    void searchWebtoonsByAuthor() {
        // Given
        Page<WebtoonViewCountResponse> page = new PageImpl<>(List.of(createDummyWebtoon()));
        when(webtoonService.searchWebtoonsByAuthor(anyString(), anyInt(), anyInt())).thenReturn(page);

        // When
        ResponseEntity<Page<WebtoonViewCountResponse>> response = webtoonController.searchWebtoonsByAuthor("작가명", 0, 10);

        // Then
        assertEquals(1, response.getBody().getContent().size());
        assertEquals("작가명", response.getBody().getContent().get(0).author());
    }

    @Test
    @DisplayName("태그 검색 - 성공")
    void searchWebtoonsByTags() {
        // Given
        Page<WebtoonViewCountResponse> page = new PageImpl<>(List.of(createDummyWebtoon()));
        when(webtoonService.searchWebtoonsByTags(anyString(), anyInt(), anyInt())).thenReturn(page);

        // When
        ResponseEntity<Page<WebtoonViewCountResponse>> response = webtoonController.searchWebtoonsByTags("코믹", 0, 10);

        // Then
        assertEquals(1, response.getBody().getContent().size());
        assertTrue(response.getBody().getContent().get(0).rankGenreTypes().contains("코믹"));
    }

    @Test
    @DisplayName("웹툰 상세 조회 - 성공")
    void getWebtoonDetail() {
        // Given
        WebtoonDetailResponse detail = createDummyDetail();
        when(webtoonService.getWebtoonDetail(anyLong())).thenReturn(detail);

        // When
        ResponseEntity<WebtoonDetailResponse> response = webtoonController.getWebtoonDetail(1L);

        // Then
        assertEquals("테스트 웹툰", response.getBody().titleName());
        assertEquals("external.url", response.getBody().webtoonPageUrl());
    }

    @Test
    @DisplayName("즐겨찾기 많은 웹툰 조회 - 성공")
    void getMostFavoritedWebtoons() {
        // Given
        List<WebtoonViewCountResponse> list = List.of(createDummyWebtoon());
        when(webtoonService.getMostFavoritedWebtoons(anyInt())).thenReturn(list);

        // When
        ResponseEntity<List<WebtoonViewCountResponse>> response = webtoonController.getMostFavoritedWebtoons(10);

        // Then
        assertEquals(1, response.getBody().size());
        assertEquals(1000L, response.getBody().get(0).totalCount());
    }

    @Test
    @DisplayName("좋아요 많은 웹툰 조회 - 성공")
    void getMostLikedWebtoons() {
        // Given
        List<WebtoonViewCountResponse> list = List.of(createDummyWebtoon());
        when(webtoonService.getMostLikedWebtoons(anyInt())).thenReturn(list);

        // When
        ResponseEntity<List<WebtoonViewCountResponse>> response = webtoonController.getMostLikedWebtoons(10);

        // Then
        assertEquals(1, response.getBody().size());
        assertEquals("테스트 웹툰", response.getBody().get(0).titleName());
    }

    @Test
    @DisplayName("봤어요 많은 웹툰 조회 - 성공")
    void getMostWatchedWebtoons() {
        // Given
        List<WebtoonViewCountResponse> list = List.of(createDummyWebtoon());
        when(webtoonService.getMostWatchedWebtoons(anyInt())).thenReturn(list);

        // When
        ResponseEntity<List<WebtoonViewCountResponse>> response = webtoonController.getMostWatchedWebtoons(10);

        // Then
        assertEquals(1, response.getBody().size());
        assertEquals("테스트 웹툰", response.getBody().get(0).titleName());
    }
}