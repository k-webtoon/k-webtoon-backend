package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;


public class WeeklyActivityDto {
    private String weekday;
    private long count;

    public WeeklyActivityDto(String weekday, long count) {
        this.weekday = weekday;
        this.count = count;
    }

    public String getWeekday() {
        return weekday;
    }

    public long getCount() {
        return count;
    }
}