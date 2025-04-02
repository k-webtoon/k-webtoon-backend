package k_webtoons.k_webtoons.log;

import k_webtoons.k_webtoons.log.logModel.UserLog;
import k_webtoons.k_webtoons.log.logRepository.UserLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {

    private final UserLogRepository userLogRepository;

    @Transactional
    public void saveLog(String logMessage) {
        UserLog userLog = UserLog.builder()
                .log(logMessage)
                .build();
        userLogRepository.save(userLog);
    }
} 