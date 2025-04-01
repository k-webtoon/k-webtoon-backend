package k_webtoons.k_webtoons.controller.webtoonComment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.webtoonComment.CommentRequestDTO;
import k_webtoons.k_webtoons.model.webtoonComment.CommentResponseDTO;
import k_webtoons.k_webtoons.service.webtoonComment.WebtoonCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class WebtoonCommentController {

    private final WebtoonCommentService service;

    // 댓글 작성
    @Operation(
            summary = "댓글 작성 API",
            description = "사용자가 웹툰 ID와 댓글 내용을 제공하여 댓글을 작성합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 작성 성공",
                            content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))
                    )
            }
    )
    @PostMapping("/{webtoonId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponseDTO> add(@PathVariable Long webtoonId, @RequestBody CommentRequestDTO requestDto) {
        CommentResponseDTO comment = service.addComment(webtoonId, requestDto);
        return ResponseEntity.ok(comment);
    }

    // 웹툰 ID로 댓글 목록 조회 API (페이징)
    @Operation(
            summary = "웹툰 ID로 댓글 목록 조회",
            description = "웹툰 ID에 해당하는 댓글 목록을 페이지네이션 형태로 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공적으로 댓글 목록 반환",
                    content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "웹툰을 찾을 수 없음"
            )
    })
    @GetMapping("/{webtoonId}")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByWebtoonId(
            @PathVariable Long webtoonId,
            @Parameter(description = "페이지 번호 (기본값: 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기 (기본값: 6)", example = "10")
            @RequestParam(defaultValue = "6") int size) {
        try {
            Page<CommentResponseDTO> comments = service.getCommentsByWebtoonId(webtoonId, page, size);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "댓글 수정 API",
            description = "댓글 ID와 새로운 내용을 제공하여 댓글을 수정합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 수정 성공",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "수정 권한 없음",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody String content) {
        try {
            service.updateComment(id, content);
            return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body("수정 권한이 없습니다.");
        }
    }

    @Operation(
            summary = "댓글 삭제 API",
            description = "댓글 ID를 제공하여 댓글을 삭제합니다 (소프트 딜리트).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 삭제 성공",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "삭제 권한 없음",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            service.deleteComment(id);
            return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body("삭제 권한이 없습니다.");
        }
    }

    @Operation(
            summary = "좋아요 추가 API",
            description = "댓글 ID를 제공하여 좋아요를 추가합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "좋아요 추가 성공",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "이미 좋아요를 누른 경우 또는 잘못된 요청",
                            content = @Content
                    )
            }
    )
    @PostMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> like(@PathVariable Long id) {
        try {
            service.addLike(id);
            return ResponseEntity.ok("좋아요가 추가되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("이미 좋아요를 누른 경우 또는 잘못된 요청입니다.");
        }
    }

    @Operation(
            summary = "좋아요 취소 API",
            description = "댓글 ID를 제공하여 좋아요를 취소합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "좋아요 취소 성공",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "좋아요 기록이 없는 경우 또는 잘못된 요청",
                            content = @Content
                    )
            }
    )
    @PostMapping("/{id}/unlike")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> unlike(@PathVariable Long id) {
        try {
            service.removeLike(id);
            return ResponseEntity.ok("좋아요가 취소되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("좋아요 기록이 없는 경우 또는 잘못된 요청입니다.");
        }
    }

    @Operation(
            summary = "베스트 댓글 조회 API",
            description = "좋아요 수가 가장 많은 상위 3개 댓글을 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "베스트 댓글 조회 성공",
                            content = @Content(schema = @Schema(implementation = CommentResponseDTO[].class))
                    )
            }
    )
    @GetMapping("/best/{webtoonId}")
    public ResponseEntity<List<CommentResponseDTO>> getBestComments(
            @Parameter(description = "웹툰 ID", required = true, example = "1")
            @PathVariable Long webtoonId
    ) {
        try {
            List<CommentResponseDTO> bestComments = service.getBestComments(webtoonId);
            return ResponseEntity.ok(bestComments);
        } catch (CustomException e) {
            if ("WEBTOON_NOT_FOUND".equals(e.getErrorCode())) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
