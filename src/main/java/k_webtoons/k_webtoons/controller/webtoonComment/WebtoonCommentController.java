package k_webtoons.k_webtoons.controller.webtoonComment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import k_webtoons.k_webtoons.model.webtoonComment.CommentRequestDTO;
import k_webtoons.k_webtoons.model.webtoonComment.CommentResponseDTO;
import k_webtoons.k_webtoons.service.webtoonComment.WebtoonCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class WebtoonCommentController {

    private final WebtoonCommentService service;

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
    public CommentResponseDTO add(@PathVariable Long webtoonId, @RequestBody CommentRequestDTO requestDto) {
        return service.addComment(webtoonId, requestDto);
    }

    @Operation(
            summary = "댓글 조회 API",
            description = "댓글 ID를 통해 특정 댓글을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 조회 성공",
                            content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public CommentResponseDTO get(@PathVariable Long id) {
        return service.getCommentById(id);
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
    public String update(@PathVariable Long id, @RequestBody String content) {
        service.updateComment(id, content);
        return "댓글이 성공적으로 수정되었습니다.";
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
    public String delete(@PathVariable Long id) {
        service.deleteComment(id);
        return "댓글이 성공적으로 삭제되었습니다.";
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
    public String like(@PathVariable Long id) {
        service.addLike(id);
        return "좋아요가 추가되었습니다.";
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
    public String unlike(@PathVariable Long id) {
        service.removeLike(id);
        return "좋아요가 취소되었습니다.";
    }
}
