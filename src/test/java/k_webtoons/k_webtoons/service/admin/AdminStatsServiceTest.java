package k_webtoons.k_webtoons.service.admin;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.admin.common.log.KeywordRankResponse;
import k_webtoons.k_webtoons.model.admin.log.PageDwellTimeResponse;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonViewCountResponse;
import k_webtoons.k_webtoons.log.logRepository.UserActivityLogRepository;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminStatsServiceTest {

    @Mock
    private UserActivityLogRepository userActivityLogRepository;

    @Mock
    private WebtoonRepository webtoonRepository;

    @InjectMocks
    private AdminStatsService adminStatsService;

    @Test
    @DisplayName("일일 활성 사용자 수 조회 - 성공")
    void 일일_활성_사용자_수_조회() {
        // Given
        when(userActivityLogRepository.countDailyActiveUsers()).thenReturn(100L);

        // When
        Long result = adminStatsService.getDailyActiveUsers();

        // Then
        assertEquals(100L, result);
        verify(userActivityLogRepository).countDailyActiveUsers();
    }

    @Test
    @DisplayName("최근 7일 방문 사용자 수 조회 - 성공")
    void 최근_7일_방문_사용자_수_조회() {
        when(userActivityLogRepository.countRecent7DaysUsers()).thenReturn(700L);
        assertEquals(700L, adminStatsService.getRecent7DaysUsers());
    }

    @Test
    @DisplayName("최근 30일 방문 사용자 수 조회 - 성공")
    void 최근_30일_방문_사용자_수_조회() {
        when(userActivityLogRepository.countRecent30DaysUsers()).thenReturn(3000L);
        assertEquals(3000L, adminStatsService.getRecent30DaysUsers());
    }

    @Test
    @DisplayName("가장 많이 조회된 웹툰 상세 조회 - 성공")
    void 가장_많이_조회된_웹툰_상세_조회() {
        // Given
        Webtoon mockWebtoon = Webtoon.builder()
                .id(1L)
                .titleName("테스트 웹툰")
                .author("작가명")
                .thumbnailUrl("url")
                .build();

        when(userActivityLogRepository.getMostVisitedWebtoonId()).thenReturn(1L);
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(mockWebtoon));

        // When
        WebtoonViewCountResponse result = adminStatsService.getMostVisitedWebtoonDetail();

        // Then
        assertEquals("테스트 웹툰", result.titleName());
        assertEquals("작가명", result.author());
    }

    @Test
    @DisplayName("가장 많이 조회된 웹툰 없을 경우 예외 발생")
    void 가장_많이_조회된_웹툰_없을_경우_예외_발생() {
        when(userActivityLogRepository.getMostVisitedWebtoonId()).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> adminStatsService.getMostVisitedWebtoonDetail());

        assertEquals("NO_VISITED_WEBTOON", exception.getErrorCode());
    }

    @Test
    @DisplayName("상위 10개 검색 키워드 조회 - 성공")
    void 상위_10개_검색_키워드_조회() {
        // Given
        List<Object[]> mockData = List.of(
                new Object[]{"액션", 150L},
                new Object[]{"로맨스", 120L}
        );

        when(userActivityLogRepository.getTop10Keywords()).thenReturn(mockData);

        // When
        List<KeywordRankResponse> results = adminStatsService.getTop10Keywords();

        // Then
        assertEquals(2, results.size());
        assertEquals("액션", results.get(0).keyword());
        assertEquals(150L, results.get(0).count());
    }

    @Test
    @DisplayName("페이지별 평균 체류 시간 통계 조회 - 성공")
    void 페이지별_평균_체류_시간_통계_조회() {
        // Given
        List<Object[]> mockData = List.of(
                new Object[]{"/main", 300},
                new Object[]{"/detail", 450}
        );

        when(userActivityLogRepository.getPageDwellTimeStats()).thenReturn(mockData);

        // When
        List<PageDwellTimeResponse> results = adminStatsService.getPageDwellTimeStats();

        // Then
        assertEquals(2, results.size());
        assertEquals("/main", results.get(0).page());
        assertEquals(300, results.get(0).avgDurationSeconds());
    }

    @Test
    @DisplayName("가장 많이 조회된 웹툰 상세 조회 실패 - 웹툰 없음")
    void 가장_많이_조회된_웹툰_상세_조회_실패_웹툰_없음() {
        // Given
        when(userActivityLogRepository.getMostVisitedWebtoonId()).thenReturn(1L);
        when(webtoonRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class,
                () -> adminStatsService.getMostVisitedWebtoonDetail());

        assertEquals("WEBTOON_NOT_FOUND", exception.getErrorCode());
    }
}