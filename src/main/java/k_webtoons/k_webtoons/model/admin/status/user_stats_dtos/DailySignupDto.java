package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailySignupDto {
    private String date;
    private Long count;

    public DailySignupDto(String date, Long count) {
        this.date = date;
        this.count = count;
    }

    public DailySignupDto() {}
}

