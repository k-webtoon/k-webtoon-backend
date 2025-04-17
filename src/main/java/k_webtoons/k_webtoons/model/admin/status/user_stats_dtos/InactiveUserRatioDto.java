package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;


public class InactiveUserRatioDto {
    private long total;
    private long inactive;
    private double ratio;

    public InactiveUserRatioDto(long total, long inactive, double ratio) {
        this.total = total;
        this.inactive = inactive;
        this.ratio = ratio;
    }

    public long getTotal() { return total; }
    public long getInactive() { return inactive; }
    public double getRatio() { return ratio; }
}