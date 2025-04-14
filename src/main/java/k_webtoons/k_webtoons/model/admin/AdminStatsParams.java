package k_webtoons.k_webtoons.model.admin;

import lombok.Data;

@Data
public class AdminStatsParams {
    private String startDate;
    private String endDate;
    private String type; // users, webtoons, authors, comments
}