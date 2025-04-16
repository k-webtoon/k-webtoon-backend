package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;


public class UserStatusRatioDto {
    private String status;
    private long count;

    public UserStatusRatioDto(String status, long count) {
        this.status = status;
        this.count = count;
    }

    public String getStatus() { return status; }
    public long getCount() { return count; }
}