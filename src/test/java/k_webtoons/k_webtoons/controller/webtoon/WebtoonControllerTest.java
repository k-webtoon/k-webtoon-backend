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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebtoonControllerTest {

    @Mock
    private WebtoonService webtoonService;

    @InjectMocks
    private WebtoonController webtoonController;

    @Test
    @DisplayName("인기 웹툰 목록 조회 테스트")
    void getTopWebtoonsTest() {
        // Given
        int page = 0;
        int size = 10;
        List<WebtoonViewCountResponse> webtoonList = new ArrayList<>();
        webtoonList.add(new WebtoonViewCountResponse(
                1L, 
                1L, 
                "인기웹툰1", 
                "작가1", 
                false, 
                "전체이용가", 
                false, 
                "thumb1.jpg", 
                "줄거리1", 
                List.of("코믹"), 
                4.5,
                3L
        ));
        webtoonList.add(new WebtoonViewCountResponse(
                2L, 
                2L, 
                "인기웹툰2", 
                "작가2", 
                false, 
                "15세", 
                false, 
                "thumb2.jpg", 
                "줄거리2", 
                List.of("판타지"), 
                4.3,
                3L
        ));
        Page<WebtoonViewCountResponse> webtoonPage = new PageImpl<>(webtoonList);
        
        when(webtoonService.getTopWebtoons(page, size)).thenReturn(webtoonPage);

        // When
        ResponseEntity<Page<WebtoonViewCountResponse>> response = webtoonController.getTopWebtoons(page, size);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getContent()).hasSize(2);
        assertThat(response.getBody().getContent().get(0).titleName()).isEqualTo("인기웹툰1");
        assertThat(response.getBody().getContent().get(1).titleName()).isEqualTo("인기웹툰2");
        
        verify(webtoonService, times(1)).getTopWebtoons(page, size);
    }

    @Test
    @DisplayName("웹툰 이름으로 검색 테스트")
    void searchWebtoonsByNameTest() {
        // Given
        String titleName = "인기";
        int page = 0;
        int size = 10;
        List<WebtoonViewCountResponse> webtoonList = new ArrayList<>();
        webtoonList.add(new WebtoonViewCountResponse(
                1L, 
                1L, 
                "인기웹툰1", 
                "작가1", 
                false, 
                "전체이용가", 
                false, 
                "thumb1.jpg", 
                "줄거리1", 
                List.of("코믹"), 
                4.5,
                3L
        ));
        Page<WebtoonViewCountResponse> webtoonPage = new PageImpl<>(webtoonList);
        
        when(webtoonService.searchWebtoonsByName(titleName, page, size)).thenReturn(webtoonPage);

        // When
        ResponseEntity<Page<WebtoonViewCountResponse>> response = webtoonController.searchWebtoonsByName(titleName, page, size);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getContent()).hasSize(1);
        assertThat(response.getBody().getContent().get(0).titleName()).isEqualTo("인기웹툰1");
        
        verify(webtoonService, times(1)).searchWebtoonsByName(titleName, page, size);
    }

    @Test
    @DisplayName("작가로 웹툰 검색 테스트")
    void searchWebtoonsByAuthorTest() {
        // Given
        String authorName = "작가1";
        int page = 0;
        int size = 10;
        List<WebtoonViewCountResponse> webtoonList = new ArrayList<>();
        webtoonList.add(new WebtoonViewCountResponse(
                1L, 
                1L, 
                "인기웹툰1", 
                "작가1", 
                false, 
                "전체이용가", 
                false, 
                "thumb1.jpg", 
                "줄거리1", 
                List.of("코믹"), 
                4.5,
                3L
        ));
        Page<WebtoonViewCountResponse> webtoonPage = new PageImpl<>(webtoonList);
        
        when(webtoonService.searchWebtoonsByAuthor(authorName, page, size)).thenReturn(webtoonPage);

        // When
        ResponseEntity<Page<WebtoonViewCountResponse>> response = webtoonController.searchWebtoonsByAuthor(authorName, page, size);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getContent()).hasSize(1);
        assertThat(response.getBody().getContent().get(0).author()).isEqualTo("작가1");
        
        verify(webtoonService, times(1)).searchWebtoonsByAuthor(authorName, page, size);
    }

    @Test
    @DisplayName("태그로 웹툰 검색 테스트")
    void searchWebtoonsByTagsTest() {
        // Given
        String tagName = "코믹";
        int page = 0;
        int size = 10;
        List<WebtoonViewCountResponse> webtoonList = new ArrayList<>();
        webtoonList.add(new WebtoonViewCountResponse(
                1L, 
                1L, 
                "인기웹툰1", 
                "작가1", 
                false, 
                "전체이용가", 
                false, 
                "thumb1.jpg", 
                "줄거리1", 
                List.of("코믹"), 
                4.5,
                3L
        ));
        Page<WebtoonViewCountResponse> webtoonPage = new PageImpl<>(webtoonList);
        
        when(webtoonService.searchWebtoonsByTags(tagName, page, size)).thenReturn(webtoonPage);

        // When
        ResponseEntity<Page<WebtoonViewCountResponse>> response = webtoonController.searchWebtoonsByTags(tagName, page, size);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getContent()).hasSize(1);
        assertThat(response.getBody().getContent().get(0).rankGenreTypes()).contains("코믹");
        
        verify(webtoonService, times(1)).searchWebtoonsByTags(tagName, page, size);
    }

    @Test
    @DisplayName("웹툰 상세 조회 테스트")
    void getWebtoonDetailTest() {
        // Given
        Long webtoonId = 1L;
        WebtoonDetailResponse detailResponse = new WebtoonDetailResponse(
                webtoonId,
                "인기웹툰1",
                "작가1",
                "thumbnail.jpg",
                "웹툰 줄거리입니다.",
                "전체이용가",
                "4.50",
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                List.of("코믹", "로맨스"),
                List.of("학원물", "성장"),
                "artist123",
                "www.www"
        );
        
        when(webtoonService.getWebtoonDetail(webtoonId)).thenReturn(detailResponse);

        // When
        ResponseEntity<WebtoonDetailResponse> response = webtoonController.getWebtoonDetail(webtoonId);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().id()).isEqualTo(webtoonId);
        assertThat(response.getBody().titleName()).isEqualTo("인기웹툰1");
        assertThat(response.getBody().author()).isEqualTo("작가1");
        assertThat(response.getBody().genre()).hasSize(2);
        assertThat(response.getBody().tag()).hasSize(2);
        
        verify(webtoonService, times(1)).getWebtoonDetail(webtoonId);
    }
} 