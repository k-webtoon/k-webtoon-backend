package k_webtoons.k_webtoons.service.webtoon;

import k_webtoons.k_webtoons.exception.WebtoonNotFoundException;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonDetailResponse;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonViewCountResponse;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebtoonServiceTest {

    @Mock
    private WebtoonRepository webtoonRepository;

    @InjectMocks
    private WebtoonService webtoonService;

    private Webtoon createWebtoonEntity() {
        return Webtoon.builder()
                .id(1L)
                .titleId(1001L)
                .titleName("테스트 웹툰")
                .author("작가명")
                .adult(false)
                .age("15")
                .finish(false)
                .thumbnailUrl("thumb.jpg")
                .synopsis("시놉시스")
                .rankGenreTypes(List.of("코믹"))
                .starScore(4.5)
                .build();
    }

    @Test
    @DisplayName("상위 웹툰 조회 - 페이지 변환 검증")
    void getTopWebtoons() {
        // Given
        Page<Webtoon> page = new PageImpl<>(List.of(createWebtoonEntity()));
        when(webtoonRepository.findTopWebtoons(any(PageRequest.class))).thenReturn(page);

        // When
        Page<WebtoonViewCountResponse> result = webtoonService.getTopWebtoons(0, 10);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals("테스트 웹툰", result.getContent().get(0).titleName());
    }

    @Test
    @DisplayName("웹툰 상세 조회 - 존재하지 않는 경우 예외 발생")
    void getWebtoonDetailNotFound() {
        // Given
        when(webtoonRepository.findByIdAndIsPublicTrue(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(WebtoonNotFoundException.class, () ->
                webtoonService.getWebtoonDetail(1L));
    }

    @Test
    @DisplayName("태그 검색 - 결과 없을 경우 빈 페이지 반환")
    void searchByTagsEmpty() {
        // Given
        Page<Webtoon> page = new PageImpl<>(List.of());
        when(webtoonRepository.findByTag(anyString(), any(PageRequest.class))).thenReturn(page);

        // When
        Page<WebtoonViewCountResponse> result = webtoonService.searchWebtoonsByTags("태그", 0, 10);

        // Then
        assertTrue(result.isEmpty());
    }
}