package k_webtoons.k_webtoons.log.logRepository;

import k_webtoons.k_webtoons.log.logModel.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLog , Long> {
}
