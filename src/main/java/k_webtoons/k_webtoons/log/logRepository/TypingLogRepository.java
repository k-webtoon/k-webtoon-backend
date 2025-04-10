package k_webtoons.k_webtoons.log.logRepository;

import k_webtoons.k_webtoons.log.logModel.TypingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypingLogRepository extends JpaRepository<TypingLog, Long> {
}
