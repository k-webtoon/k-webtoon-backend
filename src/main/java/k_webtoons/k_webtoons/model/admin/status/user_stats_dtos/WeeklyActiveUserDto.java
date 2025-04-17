package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;


public class WeeklyActiveUserDto {
    private long count;
    private String fromDate;
    private String toDate;

    public WeeklyActiveUserDto(long count, String fromDate, String toDate) {
        this.count = count;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public long getCount() { return count; }
    public String getFromDate() { return fromDate; }
    public String getToDate() { return toDate; }
}