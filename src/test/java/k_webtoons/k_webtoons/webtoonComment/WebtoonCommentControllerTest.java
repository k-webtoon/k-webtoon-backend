package k_webtoons.k_webtoons.webtoonComment;

import com.fasterxml.jackson.databind.ObjectMapper;
import k_webtoons.k_webtoons.model.webtoonComment.CommentRequestDTO;
import k_webtoons.k_webtoons.model.webtoonComment.CommentResponseDTO;
import k_webtoons.k_webtoons.service.webtoonComment.WebtoonCommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest  // 전체 애플리케이션 컨텍스트를 로드
@AutoConfigureMockMvc
public class WebtoonCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc는 HTTP 요청을 시뮬레이션 할 수 있게 해줍니다.

    @Autowired
    private ObjectMapper objectMapper;  // JSON 변환에 사용되는 ObjectMapper

    @MockBean
    private WebtoonCommentService service;  // WebtoonCommentService는 Mock으로 주입합니다.

    // 댓글 추가 테스트
    @Test
    @WithMockUser  // 보통 Spring Security를 사용하는 경우, 인증된 사용자로 테스트를 실행
    public void testAddComment() throws Exception {
        Long webtoonId = 1L;
        CommentRequestDTO requestDto = new CommentRequestDTO("Test comment");
        CommentResponseDTO responseDto = new CommentResponseDTO(1L, "Test comment", "user", LocalDateTime.now(), 0L, false);

        // service.addComment()가 호출될 때, Mock된 responseDto를 리턴하도록 설정
        given(service.addComment(eq(webtoonId), any(CommentRequestDTO.class))).willReturn(responseDto);

        // HTTP POST 요청 시뮬레이션
        mockMvc.perform(post("/api/comments/{webtoonId}", webtoonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))  // JSON 형식으로 requestDto를 보내기
                .andExpect(status().isOk())  // HTTP 상태 코드가 200이어야 함
                .andExpect(jsonPath("$.id").value(1L))  // 반환된 JSON에서 id 값이 1L이어야 함
                .andExpect(jsonPath("$.content").value("Test comment"));  // 반환된 JSON에서 content 값이 "Test comment"이어야 함
    }

    // 댓글 조회 테스트
    @Test
    @WithMockUser  // 보통 Spring Security를 사용하는 경우, 인증된 사용자로 테스트를 실행
    public void testGetComment() throws Exception {
        Long commentId = 1L;
        CommentResponseDTO responseDto = new CommentResponseDTO(commentId, "Test comment", "user", LocalDateTime.now(), 0L, false);

        // service.getCommentById()가 호출될 때, Mock된 responseDto를 리턴하도록 설정
        given(service.getCommentById(commentId)).willReturn(responseDto);

        // HTTP GET 요청 시뮬레이션
        mockMvc.perform(get("/api/comments/{id}", commentId))
                .andExpect(status().isOk())  // HTTP 상태 코드가 200이어야 함
                .andExpect(jsonPath("$.id").value(commentId))  // 반환된 JSON에서 id 값이 commentId이어야 함
                .andExpect(jsonPath("$.content").value("Test comment"));  // 반환된 JSON에서 content 값이 "Test comment"이어야 함
    }

    // 댓글 수정 테스트
    @Test
    @WithMockUser  // 보통 Spring Security를 사용하는 경우, 인증된 사용자로 테스트를 실행
    public void testUpdateComment() throws Exception {
        Long commentId = 1L;
        String newContent = "Updated comment";

        // HTTP PUT 요청 시뮬레이션
        mockMvc.perform(put("/api/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newContent))  // 새로운 content를 요청 본문에 추가
                .andExpect(status().isOk())  // HTTP 상태 코드가 200이어야 함
                .andExpect(content().string("댓글이 성공적으로 수정되었습니다."));  // 응답 메시지가 이 값이어야 함

        // service.updateComment()가 실제로 호출되었는지 확인
        verify(service).updateComment(commentId, newContent);
    }

    // 댓글 삭제 테스트
    @Test
    @WithMockUser  // 보통 Spring Security를 사용하는 경우, 인증된 사용자로 테스트를 실행
    public void testDeleteComment() throws Exception {
        Long commentId = 1L;

        // HTTP DELETE 요청 시뮬레이션
        mockMvc.perform(delete("/api/comments/{id}", commentId))
                .andExpect(status().isOk())  // HTTP 상태 코드가 200이어야 함
                .andExpect(content().string("댓글이 성공적으로 삭제되었습니다."));  // 응답 메시지가 이 값이어야 함

        // service.deleteComment()가 실제로 호출되었는지 확인
        verify(service).deleteComment(commentId);
    }

    // 좋아요 추가 테스트
    @Test
    @WithMockUser  // 보통 Spring Security를 사용하는 경우, 인증된 사용자로 테스트를 실행
    public void testLikeComment() throws Exception {
        Long commentId = 1L;

        // HTTP POST 요청 시뮬레이션
        mockMvc.perform(post("/api/comments/{id}/like", commentId))
                .andExpect(status().isOk())  // HTTP 상태 코드가 200이어야 함
                .andExpect(content().string("좋아요가 추가되었습니다."));  // 응답 메시지가 이 값이어야 함

        // service.addLike()가 실제로 호출되었는지 확인
        verify(service).addLike(commentId);
    }

    // 좋아요 취소 테스트
    @Test
    @WithMockUser  // 보통 Spring Security를 사용하는 경우, 인증된 사용자로 테스트를 실행
    public void testUnlikeComment() throws Exception {
        Long commentId = 1L;

        // HTTP POST 요청 시뮬레이션
        mockMvc.perform(post("/api/comments/{id}/unlike", commentId))
                .andExpect(status().isOk())  // HTTP 상태 코드가 200이어야 함
                .andExpect(content().string("좋아요가 취소되었습니다."));  // 응답 메시지가 이 값이어야 함

        // service.removeLike()가 실제로 호출되었는지 확인
        verify(service).removeLike(commentId);
    }
}
