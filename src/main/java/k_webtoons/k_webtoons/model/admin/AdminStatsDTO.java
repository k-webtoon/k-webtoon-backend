package k_webtoons.k_webtoons.model.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsDTO {
    // 대시보드 요약
    private long totalUsers;
    private long totalWebtoons;
    private long totalComments;

    // 사용자 통계
    private long totalViews;
    private long dailyActiveCount;
    private long monthlyActiveCount;
    private double avgSessionTime;
    private double newUserGrowthRate;
    private long recent7DaysUsers;
    private long recent30DaysUsers;

    // 웹툰 통계
    private long reportedCount;
    private long osmConversionCount;
    private double averageRating;
    private long totalWebtoonViews;

    // 댓글 통계
    private double avgDailyComments;
    private double avgCommentLength;
} 