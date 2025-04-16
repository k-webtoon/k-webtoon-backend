package k_webtoons.k_webtoons.log.logRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    // 로그 통해 가장 많이 본 웹툰 id 찾기
    public Long getMostVisitedWebtoonId() {
        String query = """
                    SELECT
                        CAST(SUBSTRING_INDEX(page, '/', -1) AS UNSIGNED) AS webtoon_id
                    FROM page_view_log
                    WHERE page REGEXP '^/webtoon/[0-9]+$'
                    GROUP BY webtoon_id
                    ORDER BY COUNT(*) DESC
                    LIMIT 1
                """;

        try {
            Object result = entityManager.createNativeQuery(query).getSingleResult();
            return result != null ? ((Number) result).longValue() : null;
        } catch (NoResultException e) {
            return null;
        }
    }

    // 검색 키워드 TOP10
    @SuppressWarnings("unchecked") // 경고 무시용
    public List<Object[]> getTop10Keywords() {
        String query = """
                    SELECT
                        keyword,
                        COUNT(keyword) AS keyword_count
                    FROM typing_log
                    GROUP BY keyword
                    ORDER BY keyword_count DESC
                    LIMIT 10
                """;
        return entityManager.createNativeQuery(query).getResultList();
    }

    // 페이지별 평균 체류 시간
    @SuppressWarnings("unchecked") // 경고 무시용
    public List<Object[]> getPageDwellTimeStats() {
        String query = """
        SELECT 
            page,
            ROUND(AVG(duration)) AS avg_duration 
        FROM page_view_log 
        WHERE duration > 0 
        GROUP BY page 
        ORDER BY avg_duration DESC
    """;
        return entityManager.createNativeQuery(query).getResultList();
    }


}
