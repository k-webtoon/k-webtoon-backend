package k_webtoons.k_webtoons.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import k_webtoons.k_webtoons.security.JwtUtil;
import k_webtoons.k_webtoons.service.user.RecommendInitService;
import k_webtoons.k_webtoons.model.user.RecommendInitRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendInitController {

    private final JwtUtil jwtUtil;
    private final RecommendInitService service;

    @PostMapping("/init")
    public ResponseEntity<?> saveInitial(@RequestBody RecommendInitRequestDTO dto,
                                         HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("토큰 없음");
        }

        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractId_init(token);

        service.saveInitialRecommendations(userId, dto);
        return ResponseEntity.ok("초기 추천 저장 완료");
    }
}