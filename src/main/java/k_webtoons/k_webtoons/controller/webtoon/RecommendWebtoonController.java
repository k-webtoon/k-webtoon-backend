package k_webtoons.k_webtoons.controller.webtoon;

import k_webtoons.k_webtoons.service.webtoon.RecommendWebtoonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webtoon/recommendations")
@RequiredArgsConstructor
public class RecommendWebtoonController {

    private final RecommendWebtoonService recommendService;

    @PostMapping("/{webtoonId}")
    public ResponseEntity<Void> toggleRecommend(@PathVariable Long webtoonId) {
        recommendService.toggleRecommend(webtoonId);
        return ResponseEntity.ok().build();
    }
}
