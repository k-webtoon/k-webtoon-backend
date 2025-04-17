package k_webtoons.k_webtoons.log.logRepository;

import k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.HourlyVisitorDto;
import k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.UserClickRankDto;
import k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.WeeklyActivityDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import k_webtoons.k_webtoons.log.logModel.ClickLog;

import java.util.List;

@Repository
public interface ClickLogRepository extends JpaRepository<ClickLog, Long> {

    @Query("""
                SELECT new k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.UserClickRankDto(
                    c.username, COUNT(c)
                )
                FROM ClickLog c
                GROUP BY c.username
                ORDER BY COUNT(c) DESC
            """)
    List<UserClickRankDto> getClickCountByUser();

    @Query("""
                SELECT new k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.HourlyVisitorDto(
                    EXTRACT(HOUR FROM c.createdAt), COUNT(DISTINCT c.username)
                )
                FROM ClickLog c
                WHERE c.createdAt >= :#{T(java.time.LocalDateTime).now().minusDays(7)}
                GROUP BY EXTRACT(HOUR FROM c.createdAt)
                ORDER BY EXTRACT(HOUR FROM c.createdAt)
            """)
    List<HourlyVisitorDto> getHourlyVisitorStats();

    @Query("""
                SELECT new k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.WeeklyActivityDto(
                    FUNCTION('TO_CHAR', c.createdAt, 'DY'), COUNT(c)
                )
                FROM ClickLog c
                WHERE c.createdAt >= :#{T(java.time.LocalDateTime).now().minusDays(30)}
                GROUP BY FUNCTION('TO_CHAR', c.createdAt, 'DY')
                ORDER BY FUNCTION('TO_CHAR', c.createdAt, 'DY')
            """)
    List<WeeklyActivityDto> getWeeklyActivityStats();
}
