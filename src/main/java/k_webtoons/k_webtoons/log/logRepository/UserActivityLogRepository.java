package k_webtoons.k_webtoons.log.logRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserActivityLogRepository {

    @PersistenceContext(unitName = "mysqlEntityManagerFactory") // MySQL용 EntityManager 지정
    private EntityManager entityManager;

    // 하루 기준 접속한 사용자 수 (중복 제거)
    public Long countDailyActiveUsers() {
        String query = """
            SELECT COUNT(DISTINCT username)
            FROM (
                SELECT username, created_at FROM click_log
                UNION
                SELECT username, created_at FROM page_view_log
                UNION
                SELECT username, created_at FROM typing_log
            ) AS combined_logs
            WHERE DATE(created_at) = CURRENT_DATE
        """;
        return ((Number) entityManager.createNativeQuery(query).getSingleResult()).longValue();
    }

    // 최근 7일간 방문한 사용자 수 (중복 제거)
    public Long countRecent7DaysUsers() {
        String query = """
            SELECT COUNT(DISTINCT username)
            FROM (
                SELECT username, created_at FROM click_log
                UNION
                SELECT username, created_at FROM page_view_log
                UNION
                SELECT username, created_at FROM typing_log
            ) AS combined_logs
            WHERE created_at >= CURRENT_DATE - INTERVAL 7 DAY
        """;
        return ((Number) entityManager.createNativeQuery(query).getSingleResult()).longValue();
    }

    // 최근 30일간 방문한 사용자 수 (중복 제거)
    public Long countRecent30DaysUsers() {
        String query = """
            SELECT COUNT(DISTINCT username)
            FROM (
                SELECT username, created_at FROM click_log
                UNION
                SELECT username, created_at FROM page_view_log
                UNION
                SELECT username, created_at FROM typing_log
            ) AS combined_logs
            WHERE created_at >= CURRENT_DATE - INTERVAL 30 DAY
        """;
        return ((Number) entityManager.createNativeQuery(query).getSingleResult()).longValue();
    }
}
