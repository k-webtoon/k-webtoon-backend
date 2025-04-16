package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;


public class AgeDistributionDto {
    private String ageGroup;
    private long count;

    public AgeDistributionDto(String ageGroup, long count) {
        this.ageGroup = ageGroup;
        this.count = count;
    }

    public String getAgeGroup() { return ageGroup; }
    public long getCount() { return count; }
}