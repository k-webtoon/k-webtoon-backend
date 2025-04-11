package k_webtoons.k_webtoons.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import k_webtoons.k_webtoons.security.HeaderValidator;
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

    private final RecommendInitService recommendInitService;
    private final HeaderValidator headerValidator;

    @PostMapping("/init")
    public ResponseEntity<String> saveInitial(
            @RequestBody RecommendInitRequestDTO dto
    ) {
        // HeaderValidator를 통한 사용자 인증
        Long userId = headerValidator.getAuthenticatedUser().getIndexId();

        recommendInitService.saveInitialRecommendations(userId, dto);
        return ResponseEntity.ok("초기 추천 저장 완료");
    }
}