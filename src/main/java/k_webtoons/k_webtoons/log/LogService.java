package k_webtoons.k_webtoons.log;

import k_webtoons.k_webtoons.log.dto.ClickLogDto;
import k_webtoons.k_webtoons.log.dto.PageViewLogDto;
import k_webtoons.k_webtoons.log.dto.TypingLogDto;
import k_webtoons.k_webtoons.log.logModel.ClickLog;
import k_webtoons.k_webtoons.log.logModel.PageViewLog;
import k_webtoons.k_webtoons.log.logModel.TypingLog;
import k_webtoons.k_webtoons.log.logRepository.ClickLogRepository;
import k_webtoons.k_webtoons.log.logRepository.PageViewLogRepository;
import k_webtoons.k_webtoons.log.logRepository.TypingLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class LogService {
    private final ClickLogRepository clickRepo;
    private final PageViewLogRepository viewRepo;
    private final TypingLogRepository typingRepo;


    public void saveClickLog(ClickLogDto dto, String username) {
        ClickLog log = new ClickLog(username, LocalDateTime.now(), dto.getPage(), dto.getTarget());
        clickRepo.save(log);
    }

    public void savePageViewLog(PageViewLogDto dto, String username) {
        PageViewLog log = new PageViewLog(username, LocalDateTime.now(),dto.getPage(), dto.getDuration());
        viewRepo.save(log);
    }

    public void saveTypingLog(TypingLogDto dto, String username) {
        TypingLog log = new TypingLog(username, LocalDateTime.now(),dto.getKeyword(), dto.getSource());
        typingRepo.save(log);
    }
}