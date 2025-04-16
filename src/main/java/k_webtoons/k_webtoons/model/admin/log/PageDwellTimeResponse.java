package k_webtoons.k_webtoons.model.admin.log;

public record PageDwellTimeResponse(
        String page,
        Integer avgDurationSeconds
) {
}
