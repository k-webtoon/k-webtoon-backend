package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;


public class HourlyVisitorDto {
    private int hour;
    private long count;

    public HourlyVisitorDto(int hour, long count) {
        this.hour = hour;
        this.count = count;
    }

    public int getHour() { return hour; }
    public long getCount() { return count; }
}