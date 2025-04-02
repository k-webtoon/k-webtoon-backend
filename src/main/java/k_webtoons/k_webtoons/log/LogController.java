package k_webtoons.k_webtoons.log;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping("/save")
    public ResponseEntity<String> saveLog(@RequestParam String message) {
        logService.saveLog(message);
        return ResponseEntity.ok("로그가 MySQL에 저장되었습니다: " + message);
    }
} 