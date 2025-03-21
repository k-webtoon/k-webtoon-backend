package k_webtoons.k_webtoons.controller.webtoonComment;

import k_webtoons.k_webtoons.model.webtoonComment.CommentRequestDTO;
import k_webtoons.k_webtoons.model.webtoonComment.CommentResponseDTO;
import k_webtoons.k_webtoons.service.webtoonComment.WebtoonCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class WebtoonCommentController {

    private final WebtoonCommentService service;

    @PostMapping("/{webtoonId}")
    public CommentResponseDTO add(@PathVariable Long webtoonId, @RequestBody CommentRequestDTO requestDto) {
        return service.addComment(webtoonId, requestDto);
    }

    @GetMapping("/{id}")
    public CommentResponseDTO get(@PathVariable Long id) {
        return service.getCommentById(id);
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody String content) {
        service.updateComment(id, content);
        return "댓글이 성공적으로 수정되었습니다.";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteComment(id);
        return "댓글이 성공적으로 삭제되었습니다.";
    }

    @PostMapping("/{id}/like")
    public String like(@PathVariable Long id) {
        service.addLike(id);
        return "좋아요가 추가되었습니다.";
    }

    @PostMapping("/{id}/unlike")
    public String unlike(@PathVariable Long id) {
        service.removeLike(id);
        return "좋아요가 취소되었습니다.";
    }
}