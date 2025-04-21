package k_webtoons.k_webtoons.controller.webtoonComment;

import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentRequestDTO;
import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentResponseDTO;
import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentWithAnalysisResponse;
import k_webtoons.k_webtoons.service.webtoonComment.WebtoonCommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebtoonCommentControllerTest {

    @Mock
    private WebtoonCommentService service;

    @InjectMocks
    private WebtoonCommentController controller;

    private CommentResponseDTO createDummyComment() {
        return new CommentResponseDTO(
                1L, "댓글 내용", "닉네임",
                LocalDateTime.now(), 5L, false
        );
    }

    @Test
    @DisplayName("댓글 작성 - 성공")
    void addComment() {
        // Given
        CommentRequestDTO req = new CommentRequestDTO("댓글 내용");
        CommentResponseDTO res = createDummyComment();
        when(service.addComment(1L, req)).thenReturn(res);

        // When
        ResponseEntity<CommentResponseDTO> response = controller.add(1L, req);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("댓글 내용", response.getBody().content());
    }

    @Test
    @DisplayName("댓글 수정 - 성공")
    void updateComment() {
        // 수정은 void 반환이므로 예외 없이 호출만 검증
        doNothing().when(service).updateComment(1L, "수정된 내용");

        ResponseEntity<String> response = controller.update(1L, "수정된 내용");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("댓글이 성공적으로 수정되었습니다.", response.getBody());
    }

    @Test
    @DisplayName("댓글 삭제 - 성공")
    void deleteComment() {
        doNothing().when(service).deleteComment(1L);

        ResponseEntity<String> response = controller.delete(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("댓글이 성공적으로 삭제되었습니다.", response.getBody());
    }

    @Test
    @DisplayName("댓글 좋아요 - 성공")
    void likeComment() {
        doNothing().when(service).addLike(1L);

        ResponseEntity<String> response = controller.like(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("좋아요가 추가되었습니다.", response.getBody());
    }

    @Test
    @DisplayName("댓글 좋아요 취소 - 성공")
    void unlikeComment() {
        doNothing().when(service).removeLike(1L);

        ResponseEntity<String> response = controller.unlike(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("좋아요가 취소되었습니다.", response.getBody());
    }

    @Test
    @DisplayName("베스트 댓글 조회 - 성공")
    void getBestComments() {
        List<CommentResponseDTO> best = List.of(createDummyComment());
        when(service.getBestComments(1L)).thenReturn(best);

        ResponseEntity<List<CommentResponseDTO>> response = controller.getBestComments(1L);

        assertEquals(1, response.getBody().size());
        assertEquals("닉네임", response.getBody().get(0).nickname());
    }

    @Test
    @DisplayName("분석 포함 댓글 목록 조회 - 성공")
    void getCommentsWithAnalysis() {
        // Given
        CommentWithAnalysisResponse mockResponse = CommentWithAnalysisResponse.builder()
                .comment(new CommentResponseDTO(1L, "댓글내용", "닉네임", LocalDateTime.now(), 5L, false))
                .feelTop3(List.of("기쁨", "신남", "행복"))
                .message1("긍정적인 댓글")
                .message2("재미있는 웹툰")
                .message3("추천하고 싶어요")
                .build();

        Page<CommentWithAnalysisResponse> page = new PageImpl<>(List.of(mockResponse));
        when(service.getCommentsWithAnalysisByWebtoonId(1L, 0, 6)).thenReturn(page);

        // When
        ResponseEntity<Page<CommentWithAnalysisResponse>> response =
                controller.getCommentsWithAnalysisByWebtoonId(1L, 0, 6);

        // Then
        assertEquals(1, response.getBody().getContent().size());
        CommentWithAnalysisResponse result = response.getBody().getContent().get(0);
        assertEquals("댓글내용", result.comment().content());
        assertEquals(3, result.feelTop3().size());
        assertEquals("긍정적인 댓글", result.message1());
    }
}