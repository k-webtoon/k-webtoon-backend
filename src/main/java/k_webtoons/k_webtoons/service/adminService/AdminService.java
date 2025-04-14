package k_webtoons.k_webtoons.service.adminService;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.exception.WebtoonNotFoundException;
import k_webtoons.k_webtoons.model.admin.FindAllUserByAdminDTO;
import k_webtoons.k_webtoons.model.admin.UserDetailByAdminDTO;
import k_webtoons.k_webtoons.model.admin.AdminStatsDTO;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonStatsResponse;
import k_webtoons.k_webtoons.security.HeaderValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import k_webtoons.k_webtoons.model.admin.DashboardSummaryDto;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import k_webtoons.k_webtoons.model.admin.AdminStatsParams;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final WebtoonRepository webtoonRepository;
    private final WebtoonCommentRepository commentRepository;
    private final HeaderValidator headerValidator;

    // 전체 사용자 수
    public long getTotalUsers() {
        return userRepository.count();
    }

    // 전체 웹툰 수
    public long getTotalWebtoons() {
        return webtoonRepository.count();
    }

    // 전체 댓글 수
    public long getTotalComments() {
        return commentRepository.count();
    }

    // 요약
    public DashboardSummaryDto getDashboardSummary() {
        return new DashboardSummaryDto(
                getTotalUsers(),
                getTotalWebtoons(),
                getTotalComments());
    }

    // 전체 사용자 찾아오기(페이지 기능)
    public Page<FindAllUserByAdminDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> new FindAllUserByAdminDTO(
                        user.getIndexId(),
                        user.getUserEmail(),
                        user.getAccountStatus().name(),
                        user.getCreateDateTime()));
    }

    public UserDetailByAdminDTO getUserDetails(Long indexId) {
        AppUser user = userRepository.findById(indexId)
                .orElseThrow(
                        () -> new CustomException("사용자를 찾을 수 없습니다. 받은 id : " + indexId, "USER NOT FOUND EXCEPTION"));
        return new UserDetailByAdminDTO(
                user.getIndexId(),
                user.getUserEmail(),
                user.getCreateDateTime(),
                user.getAccountStatus(),
                user.getUserAge(),
                user.getGender(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getSecurityQuestion());
    }

    // 웹툰 비공개 처리

    @Transactional
    public void setWebtoonPrivate(Long webtoonId) {
        // 1. 관리자 권한 확인
        AppUser admin = headerValidator.getAuthenticatedUser();
        if (!"ADMIN".equals(admin.getRole())) {
            throw new CustomException("관리자 권한이 없습니다", "ADMIN_ACCESS_DENIED");
        }

        // 2. 웹툰 존재 여부 확인
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new WebtoonNotFoundException("웹툰을 찾을 수 없습니다"));

        // 3. 비공개 처리
        webtoon.setIsPublic(false);
    }

    /// 소원이 추가해;;

// 📌 소원이 추가해준 통계 관련 메서드들
    public AdminStatsDTO getAllStats() {
        return AdminStatsDTO.builder()
                // 요약 통계
                .totalUsers(userRepository.count())
                .totalWebtoons(webtoonRepository.count())
                .totalComments(commentRepository.count())

                // 사용자 통계
                .totalViews(calculateTotalViews())
                .dailyActiveCount(calculateDailyActiveUsers())
                .monthlyActiveCount(calculateMonthlyActiveUsers())
                .avgSessionTime(calculateAverageSessionTime())
                .newUserGrowthRate(calculateNewUserGrowthRate())
                .recent7DaysUsers(calculateRecent7DaysUsers())
                .recent30DaysUsers(calculateRecent30DaysUsers())

                // 웹툰 통계
                .reportedCount(calculateReportedCount())
                .osmConversionCount(webtoonRepository.countOsmuOXNotNull())
                .averageRating(calculateAverageRating())
                .totalWebtoonViews(calculateTotalWebtoonViews())

                // 댓글 통계
                .avgDailyComments(calculateAverageDailyComments())
                .avgCommentLength(calculateAverageCommentLength())
                .build();
    }

    private long calculateTotalViews() {
        return 0;
    } // TODO

    private long calculateDailyActiveUsers() {
        return 0;
    } // TODO

    private long calculateMonthlyActiveUsers() {
        return 0;
    } // TODO

    private double calculateAverageSessionTime() {
        return 0;
    } // TODO

    private double calculateNewUserGrowthRate() {
        return 0;
    } // TODO

    private long calculateRecent7DaysUsers() {
        return 0;
    } // TODO

    private long calculateRecent30DaysUsers() {
        return 0;
    } // TODO

    private long calculateReportedCount() {
        return 0;
    } // TODO

    private double calculateAverageRating() {
        return 0;
    } // TODO

    private long calculateTotalWebtoonViews() {
        return 0;
    } // TODO

    private double calculateAverageDailyComments() {
        return 0;
    } // TODO

    private double calculateAverageCommentLength() {
        return 0;
    } // TODO

    public AdminStatsDTO getUserStats(AdminStatsParams params) {
        return AdminStatsDTO.builder()
                .totalUsers(0)
                .totalWebtoons(0)
                .totalComments(0)
                .build(); // TODO: 실제 계산 로직
    }

    public WebtoonStatsResponse getWebtoonStats(AdminStatsParams params) {
        return WebtoonStatsResponse.builder()
                .summary(WebtoonStatsResponse.Summary.builder()
                        .totalWebtoons(120)
                        .reportedWebtoons(5)
                        .osmConversions(10)
                        .totalViews(15000L)
                        .build())
                .statusDistribution(List.of(
                        WebtoonStatsResponse.StatusDistribution.builder().status("연재중").count(80).build(),
                        WebtoonStatsResponse.StatusDistribution.builder().status("완결").count(30).build(),
                        WebtoonStatsResponse.StatusDistribution.builder().status("휴재").count(10).build()))
                .topRatedWebtoons(List.of(
                        WebtoonStatsResponse.TopRatedWebtoon.builder().name("화산귀환").rating(4.92).episodes(100).build(),
                        WebtoonStatsResponse.TopRatedWebtoon.builder().name("나혼렙").rating(4.88).episodes(150).build()))
                .recentWebtoons(List.of(
                        WebtoonStatsResponse.RecentWebtoon.builder().name("던전리셋").date("2024-04-10").genre("판타지").build(),
                        WebtoonStatsResponse.RecentWebtoon.builder().name("세기말 키드").date("2024-04-09").genre("액션").build()))
                .activityStats(List.of(
                        WebtoonStatsResponse.ActivityStat.builder().name("전지적 독자 시점").comments(120).likes(350).views(8000).build(),
                        WebtoonStatsResponse.ActivityStat.builder().name("화산귀환").comments(90).likes(310).views(7000).build()))
                .monthlyStats(WebtoonStatsResponse.MonthlyStats.builder()
                        .totalViews(12000L)
                        .totalActivity(24500L)
                        .build())
                .build();
    }

    public AdminStatsDTO getCommentStats(AdminStatsParams params) {
        return AdminStatsDTO.builder()
                .totalComments(commentRepository.count())
                .build(); // TODO: 평균 길이/일일평균은 추후 계산
    }
}
