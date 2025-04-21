package k_webtoons.k_webtoons.controller.admin;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.admin.common.log.KeywordRankResponse;
import k_webtoons.k_webtoons.model.admin.common.log.StatResponse;
import k_webtoons.k_webtoons.service.admin.AdminStatsService;
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
class AdminLogStatsControllerTest {

    @Mock private AdminStatsService adminStatsService;
    @InjectMocks private AdminLogStatsController controller;

    @Test
    @DisplayName("일일 활성 사용자 수 조회 - 성공")
    void getDailyActiveUsers_성공() {
        when(adminStatsService.getDailyActiveUsers()).thenReturn(150L);
        ResponseEntity<StatResponse> response = controller.getDailyActiveUsers();
        assertEquals(150L, response.getBody().value());
    }

    @Test
    @DisplayName("최근 7일 사용자 수 조회 - 성공")
    void getRecent7DaysUsers_성공() {
        // Given
        when(adminStatsService.getRecent7DaysUsers()).thenReturn(1000L);

        // When
        ResponseEntity<StatResponse> response = controller.getRecent7DaysUsers();

        // Then
        assertEquals(1000L, response.getBody().value());
    }

    @Test
    @DisplayName("가장 많이 조회된 웹툰 상세 조회 - 데이터 없을 때 예외")
    void getMostVisitedWebtoonDetail_데이터없음_예외() {
        // Given
        when(adminStatsService.getMostVisitedWebtoonDetail())
                .thenThrow(new CustomException("가장 많이 조회된 웹툰이 없습니다", "NO_VISITED_WEBTOON"));

        // When & Then
        assertThrows(CustomException.class,
                () -> controller.getMostVisitedWebtoonDetail());
    }

    @Test
    @DisplayName("TOP 10 키워드 조회 - 성공")
    void getTop10Keywords_성공() {
        // Given
        List<KeywordRankResponse> mockData = List.of(
                new KeywordRankResponse("액션", 150L),
                new KeywordRankResponse("로맨스", 100L)
        );
        when(adminStatsService.getTop10Keywords()).thenReturn(mockData);

        // When
        ResponseEntity<List<KeywordRankResponse>> response = controller.getTop10Keywords();

        // Then
        assertEquals(2, response.getBody().size());
        assertEquals("액션", response.getBody().get(0).keyword());
    }
}