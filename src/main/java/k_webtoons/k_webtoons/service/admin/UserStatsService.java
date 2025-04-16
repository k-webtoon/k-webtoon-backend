package k_webtoons.k_webtoons.service.admin;


import k_webtoons.k_webtoons.log.logRepository.ClickLogRepository;
import k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.*;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatsService {
    private final UserRepository userRepository;
    private final ClickLogRepository clickLogRepository;


    public long getTotalUserCount() {
        return userRepository.countTotalUsers();
    }


    public List<UserStatusRatioDto> getUserStatusRatio() {
        return userRepository.countByStatus();
    }

    public InactiveUserRatioDto getInactiveUserRatio() {
        long total = userRepository.countTotalUsers();
        long inactive = userRepository.countInactiveSince(LocalDate.now().minusDays(30));
        double ratio = total == 0 ? 0.0 : (double) inactive / total;
        return new InactiveUserRatioDto(total, inactive, ratio);
    }

    public WeeklyActiveUserDto getWeeklyActiveUsers() {
        long count = userRepository.countActiveSince(LocalDate.now().minusDays(7));
        return new WeeklyActiveUserDto(count, LocalDate.now().minusDays(7).toString(), LocalDate.now().toString());
    }

    public List<DailySignupDto> getDailySignupCounts() {
        return userRepository.getSignupCountsLast30Days();
    }

    public List<AgeDistributionDto> getAgeDistribution() {
        return userRepository.countByAgeGroup();
    }

    public List<GenderRatioDto> getGenderDistribution() {
        return userRepository.countByGender();
    }

    public List<GenderAgeActivityDto> getGenderAgeActivity() {
        return userRepository.getGenderAgeActivity();
    }

    public List<UserClickRankDto> getClickRankByUser() {
        return clickLogRepository.getClickCountByUser();
    }

    public List<HourlyVisitorDto> getHourlyVisitors() {
        return clickLogRepository.getHourlyVisitorStats();
    }

    public List<WeeklyActivityDto> getWeeklyActivityPattern() {
        return clickLogRepository.getWeeklyActivityStats();
    }
}