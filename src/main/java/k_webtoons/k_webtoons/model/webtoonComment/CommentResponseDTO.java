package k_webtoons.k_webtoons.model.webtoonComment;

import java.time.LocalDateTime;

public record CommentResponseDTO(
    Long id,
    String content,
    String userNickname,
    LocalDateTime createdDate,
    Long likeCount,
    boolean isLiked
) {
}
