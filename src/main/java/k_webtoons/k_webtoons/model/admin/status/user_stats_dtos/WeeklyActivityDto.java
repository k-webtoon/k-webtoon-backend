package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;


public class WeeklyActivityDto {
    private String weekday;
    private Long count;

    public WeeklyActivityDto(Object weekday, Long count) {
        this.weekday = String.valueOf(weekday);
        this.count = count;
    }

    public String getWeekday() {
        return weekday;
    }

    public Long getCount() {
        return count;
    }
}
