//package k_webtoons.k_webtoons.controller.webtoonComment;
//
//import k_webtoons.k_webtoons.exception.CustomException;
//import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentRequestDTO;
//import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentResponseDTO;
//import k_webtoons.k_webtoons.service.webtoonComment.WebtoonCommentService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.ResponseEntity;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class WebtoonCommentControllerTest {
//
//    @Mock
//    private WebtoonCommentService commentService;
//
//    @InjectMocks
//    private WebtoonCommentController commentController;
//
//    @Test
//    @DisplayName("댓글 작성 성공 테스트")
//    void addCommentTest() {
//        // Given
//        Long webtoonId = 1L;
//        CommentRequestDTO requestDto = new CommentRequestDTO("이 웹툰 좋아요!");
//
//        LocalDateTime now = LocalDateTime.now();
//        CommentResponseDTO expectedResponse = new CommentResponseDTO(
//                1L,
//                "이 웹툰 좋아요!",
//                "테스트유저",
//                now,
//                0L,
//                false
//        );
//
//        when(commentService.addComment(anyLong(), any(CommentRequestDTO.class))).thenReturn(expectedResponse);
//
//        // When
//        ResponseEntity<CommentResponseDTO> response = commentController.add(webtoonId, requestDto);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        assertThat(response.getBody()).isEqualTo(expectedResponse);
//        assertThat(response.getBody().content()).isEqualTo("이 웹툰 좋아요!");
//
//        verify(commentService, times(1)).addComment(webtoonId, requestDto);
//    }
//
//    @Test
//    @DisplayName("웹툰 댓글 목록 조회 성공 테스트")
//    void getCommentsByWebtoonIdSuccessTest() {
//        // Given
//        Long webtoonId = 1L;
//        int page = 0;
//        int size = 6;
//
//        List<CommentResponseDTO> commentList = new ArrayList<>();
//        LocalDateTime now = LocalDateTime.now();
//        commentList.add(new CommentResponseDTO(1L, "첫 번째 댓글", "유저1", now, 3L, false));
//        commentList.add(new CommentResponseDTO(2L, "두 번째 댓글", "유저2", now, 5L, true));
//
//        Page<CommentResponseDTO> comments = new PageImpl<>(commentList);
//
//        when(commentService.getCommentsByWebtoonId(webtoonId, page, size)).thenReturn(comments);
//
//        // When
//        ResponseEntity<Page<CommentResponseDTO>> response = commentController.getCommentsByWebtoonId(webtoonId, page, size);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        assertThat(response.getBody().getContent()).hasSize(2);
//        assertThat(response.getBody().getContent().get(0).content()).isEqualTo("첫 번째 댓글");
//        assertThat(response.getBody().getContent().get(1).content()).isEqualTo("두 번째 댓글");
//
//        verify(commentService, times(1)).getCommentsByWebtoonId(webtoonId, page, size);
//    }
//
//    @Test
//    @DisplayName("웹툰 댓글 목록 조회 실패 - 웹툰 없음")
//    void getCommentsByWebtoonIdFailTest() {
//        // Given
//        Long webtoonId = 999L;
//        int page = 0;
//        int size = 6;
//
//        when(commentService.getCommentsByWebtoonId(webtoonId, page, size))
//                .thenThrow(new CustomException("웹툰을 찾을 수 없습니다.", "WEBTOON_NOT_FOUND"));
//
//        // When
//        ResponseEntity<Page<CommentResponseDTO>> response = commentController.getCommentsByWebtoonId(webtoonId, page, size);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(404);
//
//        verify(commentService, times(1)).getCommentsByWebtoonId(webtoonId, page, size);
//    }
//
//    @Test
//    @DisplayName("댓글 수정 성공 테스트")
//    void updateCommentSuccessTest() {
//        // Given
//        Long commentId = 1L;
//        String newContent = "수정된 댓글 내용";
//
//        doNothing().when(commentService).updateComment(commentId, newContent);
//
//        // When
//        ResponseEntity<String> response = commentController.update(commentId, newContent);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        assertThat(response.getBody()).contains("성공적으로 수정");
//
//        verify(commentService, times(1)).updateComment(commentId, newContent);
//    }
//
//    @Test
//    @DisplayName("댓글 수정 실패 - 권한 없음")
//    void updateCommentFailTest() {
//        // Given
//        Long commentId = 1L;
//        String newContent = "수정된 댓글 내용";
//
//        doThrow(new RuntimeException("수정 권한이 없습니다.")).when(commentService).updateComment(commentId, newContent);
//
//        // When
//        ResponseEntity<String> response = commentController.update(commentId, newContent);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(403);
//        assertThat(response.getBody()).contains("수정 권한이 없습니다");
//
//        verify(commentService, times(1)).updateComment(commentId, newContent);
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 성공 테스트")
//    void deleteCommentSuccessTest() {
//        // Given
//        Long commentId = 1L;
//
//        doNothing().when(commentService).deleteComment(commentId);
//
//        // When
//        ResponseEntity<String> response = commentController.delete(commentId);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        assertThat(response.getBody()).contains("성공적으로 삭제");
//
//        verify(commentService, times(1)).deleteComment(commentId);
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 실패 - 권한 없음")
//    void deleteCommentFailTest() {
//        // Given
//        Long commentId = 1L;
//
//        doThrow(new RuntimeException("삭제 권한이 없습니다.")).when(commentService).deleteComment(commentId);
//
//        // When
//        ResponseEntity<String> response = commentController.delete(commentId);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(403);
//        assertThat(response.getBody()).contains("삭제 권한이 없습니다");
//
//        verify(commentService, times(1)).deleteComment(commentId);
//    }
//
//    @Test
//    @DisplayName("댓글 좋아요 추가 성공 테스트")
//    void likeCommentSuccessTest() {
//        // Given
//        Long commentId = 1L;
//
//        doNothing().when(commentService).addLike(commentId);
//
//        // When
//        ResponseEntity<String> response = commentController.like(commentId);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        assertThat(response.getBody()).contains("좋아요가 추가");
//
//        verify(commentService, times(1)).addLike(commentId);
//    }
//
//    @Test
//    @DisplayName("댓글 좋아요 추가 실패 - 이미 좋아요 누름")
//    void likeCommentFailTest() {
//        // Given
//        Long commentId = 1L;
//
//        doThrow(new RuntimeException("이미 좋아요를 눌렀습니다.")).when(commentService).addLike(commentId);
//
//        // When
//        ResponseEntity<String> response = commentController.like(commentId);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(400);
//        assertThat(response.getBody()).contains("이미 좋아요를 누른 경우");
//
//        verify(commentService, times(1)).addLike(commentId);
//    }
//
//    @Test
//    @DisplayName("댓글 좋아요 취소 성공 테스트")
//    void unlikeCommentSuccessTest() {
//        // Given
//        Long commentId = 1L;
//
//        doNothing().when(commentService).removeLike(commentId);
//
//        // When
//        ResponseEntity<String> response = commentController.unlike(commentId);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        assertThat(response.getBody()).contains("좋아요가 취소");
//
//        verify(commentService, times(1)).removeLike(commentId);
//    }
//
//    @Test
//    @DisplayName("베스트 댓글 조회 테스트")
//    void getBestCommentsTest() {
//        // Given
//        Long webtoonId = 1L;
//        List<CommentResponseDTO> bestComments = new ArrayList<>();
//        LocalDateTime now = LocalDateTime.now();
//        bestComments.add(new CommentResponseDTO(1L, "인기 댓글1", "유저1", now, 10L, false));
//        bestComments.add(new CommentResponseDTO(2L, "인기 댓글2", "유저2", now, 8L, false));
//        bestComments.add(new CommentResponseDTO(3L, "인기 댓글3", "유저3", now, 6L, false));
//
//        when(commentService.getBestComments(webtoonId)).thenReturn(bestComments);
//
//        // When
//        ResponseEntity<List<CommentResponseDTO>> response = commentController.getBestComments(webtoonId);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        assertThat(response.getBody()).hasSize(3);
//        assertThat(response.getBody().get(0).content()).isEqualTo("인기 댓글1");
//        assertThat(response.getBody().get(1).content()).isEqualTo("인기 댓글2");
//        assertThat(response.getBody().get(2).content()).isEqualTo("인기 댓글3");
//
//        verify(commentService, times(1)).getBestComments(webtoonId);
//    }
//}