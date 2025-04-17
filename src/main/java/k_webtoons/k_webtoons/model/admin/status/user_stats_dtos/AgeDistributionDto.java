package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;

public class AgeDistributionDto {
    private Integer userAge;
    private long count;

    public AgeDistributionDto(Integer userAge, long count) {
        this.userAge = userAge;
        this.count = count;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public long getCount() {
        return count;
    }
}
