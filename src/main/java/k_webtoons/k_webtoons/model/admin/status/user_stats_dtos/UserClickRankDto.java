package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;


public class UserClickRankDto {
    private String username;
    private long count;

    public UserClickRankDto(String username, long count) {
        this.username = username;
        this.count = count;
    }

    public String getUsername() { return username; }
    public long getCount() { return count; }
}