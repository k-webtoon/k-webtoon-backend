package k_webtoons.k_webtoons.service.adminService;

import k_webtoons.k_webtoons.log.logRepository.UserActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

   private final UserActivityLogRepository userActivityLogRepository;


    // 하루 기준 접속한 사용자 수
    public Long getDailyActiveUsers() {
        return userActivityLogRepository.countDailyActiveUsers();
    }

    // 최근 7일간 방문한 사용자 수
    public Long getRecent7DaysUsers() {
        return userActivityLogRepository.countRecent7DaysUsers();
    }

    // 최근 30일간 방문한 사용자 수
    public Long getRecent30DaysUsers() {
        return userActivityLogRepository.countRecent30DaysUsers();
    }
}
