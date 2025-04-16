package k_webtoons.k_webtoons.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stats/webtoons")
@RequiredArgsConstructor
public class AdminWebtoonStatsController {
    // TODO: 전체 웹툰 수
    @GetMapping("/count")
    public ResponseEntity<?> getTotalWebtoonCount() {
        return ResponseEntity.ok().build();
    }

    // TODO: 장르별 웹툰 수
    @GetMapping("/genre-distribution")
    public ResponseEntity<?> getGenreDistribution() {
        return ResponseEntity.ok().build();
    }

    // TODO: OSMU 콘텐츠 비율
    @GetMapping("/osmu-ratio")
    public ResponseEntity<?> getOsmuRatio() {
        return ResponseEntity.ok().build();
    }

    // TODO: 평균 평점 및 표준편차
    @GetMapping("/score-stats")
    public ResponseEntity<?> getScoreStats() {
        return ResponseEntity.ok().build();
    }

    // TODO: 인기 웹툰 TOP10
    @GetMapping("/top-rated")
    public ResponseEntity<?> getTopRatedWebtoons() {
        return ResponseEntity.ok().build();
    }

    // TODO: 완결/연재 상태 비율
    @GetMapping("/finish-ratio")
    public ResponseEntity<?> getFinishRatio() {
        return ResponseEntity.ok().build();
    }

    // TODO: 공개/비공개 비율
    @GetMapping("/public-ratio")
    public ResponseEntity<?> getPublicRatio() {
        return ResponseEntity.ok().build();
    }

    // TODO: 전체 댓글 수
    @GetMapping("/comments/count")
    public ResponseEntity<?> getTotalCommentCount() {
        return ResponseEntity.ok().build();
    }

    // TODO: 삭제된 댓글 비율
    @GetMapping("/comments/deleted-ratio")
    public ResponseEntity<?> getDeletedCommentRatio() {
        return ResponseEntity.ok().build();
    }

    // TODO: 일자별 댓글 수
    @GetMapping("/comments/daily-count")
    public ResponseEntity<?> getDailyCommentCount() {
        return ResponseEntity.ok().build();
    }

    // TODO: 댓글 많은 웹툰 TOP10
    @GetMapping("/comments/top")
    public ResponseEntity<?> getTopCommentedWebtoons() {
        return ResponseEntity.ok().build();
    }

    // TODO: 일별 댓글 랭킹 TOP10
    @GetMapping("/comments/top-by-day")
    public ResponseEntity<?> getDailyTopCommentedWebtoons() {
        return ResponseEntity.ok().build();
    }

    // TODO: 웹툰 페이지 체류시간 랭킹
    @GetMapping("/viewtime-rank")
    public ResponseEntity<?> getViewTimeRank() {
        return ResponseEntity.ok().build();
    }
}
