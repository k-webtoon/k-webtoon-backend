package k_webtoons.k_webtoons.model.webtoonComment.dto;

import k_webtoons.k_webtoons.model.webtoonComment.CommentLike;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponseDTO(
        Long id,
        String content,
        String nickname,
        java.time.LocalDateTime createdDate,
        Long likeCount,
        boolean isLiked
) {
}