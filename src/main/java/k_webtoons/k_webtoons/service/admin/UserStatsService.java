package k_webtoons.k_webtoons.service.admin;


import k_webtoons.k_webtoons.log.logRepository.ClickLogRepository;
import k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.*;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatsService {
    private final UserRepository userRepository;
    private final ClickLogRepository clickLogRepository;


    public long getTotalUserCount() {
        return userRepository.countTotalUsers();
    }


    public List<DailySignupDto> getDailySignupCounts() {
        LocalDateTime start = LocalDateTime.now().minusDays(30);
        return userRepository.getSignupCountsLast30Days(start)
                .stream()
                .map(p -> new DailySignupDto(p.getDate(), p.getCount()))
                .toList();
    }


    // 사용자 상태별 비율을 조회합니다. (예: ACTIVE, SUSPENDED, DEACTIVATED)
    public List<UserStatusRatioDto> getUserStatusRatio() {
        return userRepository.countByStatus();
    }

    // 연령대별 사용자 분포를 조회합니다. (예: 10대, 20대, 30대 등)
    public List<AgeDistributionDto> getAgeDistribution() {
        return userRepository.countByAgeGroup();
    }

    // 성별별 사용자 비율을 조회합니다. (예: 남성, 여성, 기타)
    public List<GenderRatioDto> getGenderDistribution() {
        return userRepository.countByGender();
    }

    // 성별 + 연령대 기준의 접속 활동 통계를 조회합니다.
    public List<GenderAgeActivityDto> getGenderAgeActivity() {
        return userRepository.getGenderAgeActivity();
    }

    // 사용자별 클릭 수 랭킹을 조회합니다. (많이 클릭한 순으로 정렬)
    public List<UserClickRankDto> getClickRankByUser() {
        return clickLogRepository.getClickCountByUser();
    }

    // 시간대별 방문자 수 통계를 조회합니다. (0시 ~ 23시 기준)
    public List<HourlyVisitorDto> getHourlyVisitors() {
        return clickLogRepository.getHourlyVisitorStats();
    }

    // 요일별 사용자 활동량 평균을 조회합니다. (월~일 기준)
    public List<WeeklyActivityDto> getWeeklyActivityPattern() {
        return clickLogRepository.getWeeklyActivityStats();
    }

    //    public InactiveUserRatioDto getInactiveUserRatio() {
//        long total = userRepository.countTotalUsers();
//        long inactive = userRepository.countInactiveSince(LocalDate.now().minusDays(30));
//        double ratio = total == 0 ? 0.0 : (double) inactive / total;
//        return new InactiveUserRatioDto(total, inactive, ratio);
//    }

//    public WeeklyActiveUserDto getWeeklyActiveUsers() {
//        long count = userRepository.countActiveSince(LocalDate.now().minusDays(7));
//        return new WeeklyActiveUserDto(count, LocalDate.now().minusDays(7).toString(), LocalDate.now().toString());
//    }
}
