package k_webtoons.k_webtoons.model.webtoon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebtoonStatsResponse {

    private Summary summary;
    private List<StatusDistribution> statusDistribution;
    private List<TopRatedWebtoon> topRatedWebtoons;
    private List<RecentWebtoon> recentWebtoons;
    private List<ActivityStat> activityStats;
    private MonthlyStats monthlyStats;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private int totalWebtoons;
        private int reportedWebtoons;
        private int osmConversions;
        private long totalViews;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusDistribution {
        private String status;
        private int count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopRatedWebtoon {
        private String name;
        private double rating;
        private int episodes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentWebtoon {
        private String name;
        private String date; // yyyy-MM-dd
        private String genre;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityStat {
        private String name;
        private int comments;
        private int likes;
        private int views;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyStats {
        private long totalViews;
        private long totalActivity;
    }
}