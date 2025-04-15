package k_webtoons.k_webtoons.controller.admin;

import k_webtoons.k_webtoons.model.admin.StatResponse;
import k_webtoons.k_webtoons.service.adminService.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    public AdminStatsController(AdminStatsService adminStatsService) {
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
}
