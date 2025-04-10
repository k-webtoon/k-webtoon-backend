package k_webtoons.k_webtoons.log.logRepository;

import k_webtoons.k_webtoons.log.logModel.PageViewLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageViewLogRepository extends JpaRepository<PageViewLog, Long> {
}