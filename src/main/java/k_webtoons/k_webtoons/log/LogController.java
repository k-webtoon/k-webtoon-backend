package k_webtoons.k_webtoons.log;

import k_webtoons.k_webtoons.log.dto.ClickLogDto;
import k_webtoons.k_webtoons.log.dto.PageViewLogDto;
import k_webtoons.k_webtoons.log.dto.TypingLogDto;
import k_webtoons.k_webtoons.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@Slf4j
public class LogController {

    private final LogService logService;
    private final JwtUtil jwtUtil;

    @PostMapping("/click")
    public ResponseEntity<Void> logClick(@RequestBody ClickLogDto dto,
                                         @RequestHeader("Authorization") String authHeader) {
        String username = jwtUtil.extractUsername(authHeader.replace("Bearer ", ""));
        System.out.println("ClickLog - username: " + username); // ✅ 추가
        logService.saveClickLog(dto, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/page-view")
    public ResponseEntity<Void> logPageView(@RequestBody PageViewLogDto dto,
                                            @RequestHeader("Authorization") String authHeader) {
        String username = jwtUtil.extractUsername(authHeader.replace("Bearer ", ""));
        logService.savePageViewLog(dto, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/typing")
    public ResponseEntity<Void> logTyping(@RequestBody TypingLogDto dto,
                                          @RequestHeader("Authorization") String authHeader) {
        String username = jwtUtil.extractUsername(authHeader.replace("Bearer ", ""));
        logService.saveTypingLog(dto, username);
        return ResponseEntity.ok().build();
    }
}
