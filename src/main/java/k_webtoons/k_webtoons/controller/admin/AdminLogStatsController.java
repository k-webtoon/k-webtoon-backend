package k_webtoons.k_webtoons.controller.admin;

import k_webtoons.k_webtoons.model.admin.status.log_stats_dtos.StatResponse;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonViewCountResponse;
import k_webtoons.k_webtoons.service.admin.LogStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminLogStatsController {

    private final LogStatsService adminStatsService;

    public AdminLogStatsController(LogStatsService adminStatsService) {
        this.adminStatsService = adminStatsService;
    }

    // 하루 접속한 사용자수
    @GetMapping("/daily-active-users")
    public ResponseEntity<StatResponse> getDailyActiveUsers() {
        Long dailyActiveUsers = adminStatsService.getDailyActiveUsers();
        return ResponseEntity.ok(new StatResponse(dailyActiveUsers));
    }

    //최근 7일간 방문한 사용자 수
    @GetMapping("/recent-7days-users")
    public ResponseEntity<StatResponse> getRecent7DaysUsers() {
        Long recent7DaysUsers = adminStatsService.getRecent7DaysUsers();
        return ResponseEntity.ok(new StatResponse(recent7DaysUsers));
    }

    // 최근 30일간 방문한 사용자 수
    @GetMapping("/recent-30days-users")
    public ResponseEntity<StatResponse> getRecent30DaysUsers() {
        Long recent30DaysUsers = adminStatsService.getRecent30DaysUsers();
        return ResponseEntity.ok(new StatResponse(recent30DaysUsers));
    }

    // 로그 기반 가장 조회가 많이된 웹툰
    @GetMapping("/most-visited-webtoon-detail")
    public ResponseEntity<WebtoonViewCountResponse> getMostVisitedWebtoonDetail() {
        return ResponseEntity.ok(adminStatsService.getMostVisitedWebtoonDetail());
    }


    // TODO: 전체 클릭 수
    @GetMapping("/logs/click-total")
    public ResponseEntity<?> getClickTotal() {
        return ResponseEntity.ok().build();
    }

    // TODO: 전체 페이지뷰 수
    @GetMapping("/logs/pageview-total")
    public ResponseEntity<?> getPageviewTotal() {
        return ResponseEntity.ok().build();
    }

    // TODO: 전체 검색 수
    @GetMapping("/logs/typing-total")
    public ResponseEntity<?> getTypingTotal() {
        return ResponseEntity.ok().build();
    }

    // TODO: 전체 로그 총합
    @GetMapping("/logs/log-total")
    public ResponseEntity<?> getLogTotal() {
        return ResponseEntity.ok().build();
    }

    // TODO: 검색 키워드 TOP10
    @GetMapping("/logs/typing-top-keywords")
    public ResponseEntity<?> getTypingTopKeywords() {
        return ResponseEntity.ok().build();
    }

    // TODO: 검색 방식 비율 (기본/AI)
    @GetMapping("/logs/typing-source-ratio")
    public ResponseEntity<?> getTypingSourceRatio() {
        return ResponseEntity.ok().build();
    }

    // TODO: 검색 방식별 시간 추이
    @GetMapping("/logs/typing-trend")
    public ResponseEntity<?> getTypingTrend() {
        return ResponseEntity.ok().build();
    }

    // TODO: 클릭 흐름 분석 (page → target)
    @GetMapping("/logs/click-flow")
    public ResponseEntity<?> getClickFlow() {
        return ResponseEntity.ok().build();
    }

    // TODO: 페이지별 평균 체류 시간
    @GetMapping("/logs/page-duration")
    public ResponseEntity<?> getPageDuration() {
        return ResponseEntity.ok().build();
    }
}
