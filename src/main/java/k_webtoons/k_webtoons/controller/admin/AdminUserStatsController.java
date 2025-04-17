package k_webtoons.k_webtoons.controller.admin;

import k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.*;
import k_webtoons.k_webtoons.service.admin.UserStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stats/users")
@RequiredArgsConstructor
public class AdminUserStatsController {

    private final UserStatsService userStatsService;

    // 전체 가입자 수
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalUserCount() {
        return ResponseEntity.ok(userStatsService.getTotalUserCount());
    }

    // 연령대 분포
    @GetMapping("/age-distribution")
    public ResponseEntity<List<AgeDistributionDto>> getAgeDistribution() {
        return ResponseEntity.ok(userStatsService.getAgeDistribution());
    }

    // 성별 비율
    @GetMapping("/gender-distribution")
    public ResponseEntity<List<GenderRatioDto>> getGenderDistribution() {
        return ResponseEntity.ok(userStatsService.getGenderDistribution());
    }

    // 성별 + 연령대 기준 접속 활동량
    @GetMapping("/gender-age-activity")
    public ResponseEntity<List<GenderAgeActivityDto>> getGenderAgeActivity() {
        return ResponseEntity.ok(userStatsService.getGenderAgeActivity());
    }

//     상태별 사용자 비율 (ACTIVE / SUSPENDED / DEACTIVATED)
    @GetMapping("/status-ratio")
    public ResponseEntity<List<UserStatusRatioDto>> getUserStatusRatio() {
        return ResponseEntity.ok(userStatsService.getUserStatusRatio());
    }

//     일자별 가입자 수
    @GetMapping("/daily-signups")
    public ResponseEntity<List<DailySignupDto>> getDailySignups() {
        return ResponseEntity.ok(userStatsService.getDailySignupCounts());
    }
    /*
     ─────────────────────────────────────────────────────
     ⛔️ 아래 API는 현재 비활성화되어 있으며 추후 사용 가능
     ─────────────────────────────────────────────────────
    */

    // 상태별 사용자 비율 (ACTIVE / SUSPENDED / DEACTIVATED)
//    @GetMapping("/status-ratio")
//    public ResponseEntity<List<UserStatusRatioDto>> getUserStatusRatio() {
//        return ResponseEntity.ok(userStatsService.getUserStatusRatio());
//    }

    // 최근 활동 없는 사용자 비율 (30일 기준)
//    @GetMapping("/inactive-ratio")
//    public ResponseEntity<InactiveUserRatioDto> getInactiveUserRatio() {
//        return ResponseEntity.ok(userStatsService.getInactiveUserRatio());
//    }

    // 최근 7일간 활성 사용자 수
//    @GetMapping("/active-weekly")
//    public ResponseEntity<WeeklyActiveUserDto> getWeeklyActiveUsers() {
//        return ResponseEntity.ok(userStatsService.getWeeklyActiveUsers());
//    }


    // 사용자별 클릭 수 랭킹
//    @GetMapping("/click-rank")
//    public ResponseEntity<List<UserClickRankDto>> getClickRankByUser() {
//        return ResponseEntity.ok(userStatsService.getClickRankByUser());
//    }

    // 시간대별 접속자 수 (0~23시)
//    @GetMapping("/hourly-visitors")
//    public ResponseEntity<List<HourlyVisitorDto>> getHourlyVisitors() {
//        return ResponseEntity.ok(userStatsService.getHourlyVisitors());
//    }

    // 요일별 사용자 활동량 (월~일 평균)
//    @GetMapping("/weekly-activity-pattern")
//    public ResponseEntity<List<WeeklyActivityDto>> getWeeklyActivityPattern() {
//        return ResponseEntity.ok(userStatsService.getWeeklyActivityPattern());
//    }
}
