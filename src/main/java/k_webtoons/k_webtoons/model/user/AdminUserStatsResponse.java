package k_webtoons.k_webtoons.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserStatsResponse {

    private Summary summary;
    private List<MonthlyNewAuthor> monthlyNewAuthors;
    private List<WorksDistribution> worksDistribution;
    private List<TopAuthor> topAuthors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private long totalAuthors;
        private double averageWorksPerAuthor;
        private double averageRating;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyNewAuthor {
        private String month;
        private int authors;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorksDistribution {
        private String works;
        private int count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopAuthor {
        private String name;
        private long totalViews;
        private double avgRating;
    }
}