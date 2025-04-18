package k_webtoons.k_webtoons.controller.admin;

import k_webtoons.k_webtoons.model.admin.common.log.KeywordRankResponse;
import k_webtoons.k_webtoons.model.admin.common.log.StatResponse;
import k_webtoons.k_webtoons.model.admin.log.PageDwellTimeResponse;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonViewCountResponse;
import k_webtoons.k_webtoons.service.admin.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/stats")
public class AdminLogStatsController {

    private final AdminStatsService adminStatsService;

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

    // TOP 10 검색 키워드
    @GetMapping("/top-keywords")
    public ResponseEntity<List<KeywordRankResponse>> getTop10Keywords() {
        return ResponseEntity.ok(adminStatsService.getTop10Keywords());
    }


    @GetMapping("/page-dwell-time")
    public ResponseEntity<List<PageDwellTimeResponse>> getPageDwellTime() {
        return ResponseEntity.ok(adminStatsService.getPageDwellTimeStats());
    }



}
