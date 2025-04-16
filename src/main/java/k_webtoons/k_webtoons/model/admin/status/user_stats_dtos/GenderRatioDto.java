package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;


public class GenderRatioDto {
    private String gender;
    private long count;

    public GenderRatioDto(String gender, long count) {
        this.gender = gender;
        this.count = count;
    }

    public String getGender() { return gender; }
    public long getCount() { return count; }
}