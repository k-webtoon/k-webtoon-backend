package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;


import java.time.LocalDate;

public class DailySignupDto {
    private LocalDate date;
    private long count;

    public DailySignupDto(LocalDate date, long count) {
        this.date = date;
        this.count = count;
    }

    public LocalDate getDate() { return date; }
    public long getCount() { return count; }
}
