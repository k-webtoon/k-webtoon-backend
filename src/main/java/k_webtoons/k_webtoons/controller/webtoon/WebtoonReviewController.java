package k_webtoons.k_webtoons.controller.webtoon;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import k_webtoons.k_webtoons.model.webtoon.dto.*;
import k_webtoons.k_webtoons.service.webtoon.WebtoonReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/webtoons/reviews")
@RequiredArgsConstructor
public class WebtoonReviewController {

    private final WebtoonReviewService reviewService;

    @Operation(summary = "좋아요 토글", description = "특정 웹툰의 좋아요 상태를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "웹툰 또는 사용자 정보를 찾을 수 없음")
    })
    @PostMapping("/{webtoonId}/like")
    public ResponseEntity<LikeDTO> toggleLike(@PathVariable Long webtoonId) {
        LikeDTO response = reviewService.toggleLike(webtoonId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 좋아요 목록 조회", description = "사용자가 좋아요를 누른 모든 웹툰 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음")
    })
    @GetMapping("/{userId}/likes")
    public ResponseEntity<List<LikeReloadDTO>> getLikes(@PathVariable Long userId) {
        List<LikeReloadDTO> response = reviewService.getLikes(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "평점 추가/수정", description = "특정 웹툰에 대해 평점을 추가하거나 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "평점 추가/수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 평점 입력"),
            @ApiResponse(responseCode = "404", description = "웹툰 또는 사용자 정보를 찾을 수 없음")
    })
    @PutMapping("/{webtoonId}/rate")
    public ResponseEntity<RatingDTO> rateWebtoon(
            @PathVariable Long webtoonId,
            @RequestBody RatingRequestDTO request) {
        RatingDTO response = reviewService.rateWebtoon(webtoonId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 평점 목록 조회", description = "사용자가 매긴 모든 평점 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "평점 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음")
    })
    @GetMapping("/{userId}/ratings")
    public ResponseEntity<List<RatingDTO>> getRatings(@PathVariable Long userId) {
        List<RatingDTO> response = reviewService.getRatings(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "즐겨찾기 토글", description = "특정 웹툰의 즐겨찾기 상태를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "즐겨찾기 상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "웹툰 또는 사용자 정보를 찾을 수 없음")
    })
    @PostMapping("/{webtoonId}/favorite")
    public ResponseEntity<FavoriteDTO> toggleFavorite(@PathVariable Long webtoonId) {
        FavoriteDTO response = reviewService.toggleFavorite(webtoonId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 즐겨찾기 목록 조회", description = "사용자가 즐겨찾기로 설정한 모든 웹툰 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "즐겨찾기 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음")
    })
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<List<FavoriteDTO>> getFavorites(@PathVariable Long userId) {
        List<FavoriteDTO> response = reviewService.getFavorites(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "봤어요 토글", description = "특정 웹툰의 봤어요 상태를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "봤어요 상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "웹툰 또는 사용자 정보를 찾을 수 없음")
    })
    @PostMapping("/{webtoonId}/watched")
    public ResponseEntity<WatchedDTO> toggleWatched(@PathVariable Long webtoonId) {
        WatchedDTO response = reviewService.toggleWatched(webtoonId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 봤어요 목록 조회", description = "사용자가 봤다고 표시한 모든 웹툰 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "봤어요 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음")
    })
    @GetMapping("/{userId}/watched")
    public ResponseEntity<List<WatchedDTO>> getWatchedList(@PathVariable Long userId) {
        List<WatchedDTO> response = reviewService.getWatchedList(userId);
        return ResponseEntity.ok(response);
    }
}

