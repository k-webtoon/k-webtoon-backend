package k_webtoons.k_webtoons.model.user;

import java.time.LocalDateTime;

public record UserCommentResponseDTO(
        Long id,
        String content,
        String nickname,
        LocalDateTime createdDate,
        Integer likeCount
) {
}
