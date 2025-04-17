package k_webtoons.k_webtoons.controller.admin;

import k_webtoons.k_webtoons.service.admin.WebtoonStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats/webtoons")
@RequiredArgsConstructor
public class AdminWebtoonStatsController {

    private final WebtoonStatsService adminWebtoonStatsService;

    // 전체 웹툰 수 조회
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalWebtoonCount() {
        return ResponseEntity.ok(adminWebtoonStatsService.getTotalWebtoonCount());
    }

    // 장르별 웹툰 수 분포 조회
    @GetMapping("/genre-distribution")
    public ResponseEntity<Map<String, Long>> getGenreDistribution() {
        return ResponseEntity.ok(adminWebtoonStatsService.getGenreDistribution());
    }

    // OSMU(2차 콘텐츠화) 비율 조회
    @GetMapping("/osmu-ratio")
    public ResponseEntity<Map<String, Long>> getOsmuRatio() {
        return ResponseEntity.ok(adminWebtoonStatsService.getOsmuRatio());
    }

    // 평균 평점 및 평점 표준편차 조회
    @GetMapping("/score-stats")
    public ResponseEntity<Map<String, Double>> getScoreStats() {
        return ResponseEntity.ok(adminWebtoonStatsService.getScoreStats());
    }

    // 전체 댓글 수 조회
    @GetMapping("/comments/count")
    public ResponseEntity<Long> getTotalCommentCount() {
        return ResponseEntity.ok(adminWebtoonStatsService.getTotalCommentCount());
    }

    // 삭제된 댓글 비율 조회
    @GetMapping("/comments/deleted-ratio")
    public ResponseEntity<Double> getDeletedCommentRatio() {
        return ResponseEntity.ok(adminWebtoonStatsService.getDeletedCommentRatio());
    }
}
