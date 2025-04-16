package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;


public class GenderAgeActivityDto {
    private String gender;
    private Integer userAge;
    private long count;

    public GenderAgeActivityDto(String gender, Integer userAge, long count) {
        this.gender = gender;
        this.userAge = userAge;
        this.count = count;
    }

    public String getGender() { return gender; }
    public Integer getUserAge() { return userAge; }
    public long getCount() { return count; }
}