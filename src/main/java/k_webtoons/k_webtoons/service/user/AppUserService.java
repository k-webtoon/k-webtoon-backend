package k_webtoons.k_webtoons.service.user;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.LikeWebtoonList;
import k_webtoons.k_webtoons.model.user.LikeWebtoonDTO;
import k_webtoons.k_webtoons.model.user.UserCommentResponseDTO;
import k_webtoons.k_webtoons.model.user.UserInfoDTO;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.repository.webtoon.LikeWebtoonListRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;
import k_webtoons.k_webtoons.service.auth.AuthService;
import k_webtoons.k_webtoons.service.webtoon.LikeWebtoonService;
import k_webtoons.k_webtoons.service.webtoon.WebtoonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final UserRepository userRepository;
    private final UserFollowService userFollowService;
    private final AuthService authService;
    private final WebtoonCommentRepository webtoonCommentRepository;
    private final LikeWebtoonListRepository likeWebtoonListRepository;

    // 사용자 정보 조회 (어드민 제외)
    public UserInfoDTO getUserInfoByUserId(Long userId) {
        try {
            AppUser user = authService.getUserByUserIdNotAdmin(userId);

            long commentCount = user.getWebtoonComments().stream()
                    .filter(comment -> comment.getDeletedDateTime() == null)
                    .count();

            long followerCount = userFollowService.getFollowerCount(user.getIndexId());
            long followeeCount = userFollowService.getFolloweeCount(user.getIndexId());

            return new UserInfoDTO(
                    user.getIndexId(),
                    user.getUserEmail(),
                    user.getNickname(),
                    user.getUserAge(),
                    user.getGender(),
                    commentCount,
                    followerCount,
                    followeeCount
            );
        } catch (Exception e) {
            throw new CustomException("사용자 정보를 불러올 수 없습니다.", "USER_INFO_ERROR");
        }
    }

    // 댓글 조회 (어드민 제외)
    public List<UserCommentResponseDTO> getCommentsByUserId(Long userId) {
        try {
            AppUser user = authService.getUserByUserIdNotAdmin(userId); // ✅ 어드민 제외
            List<WebtoonComment> comments = webtoonCommentRepository.findByUserIdAndDeletedDateTimeIsNull(user.getIndexId());

            if (comments.isEmpty()) {
                return List.of();
            }

            return comments.stream()
                    .map(comment -> new UserCommentResponseDTO(
                            comment.getId(),
                            comment.getContent(),
                            comment.getAppUser().getNickname(),
                            comment.getCreatedDate(),
                            comment.getLikes().size()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("댓글을 불러올 수 없습니다.", "COMMENT_ERROR");
        }
    }

    // 좋아요한 웹툰 조회 (어드민 제외)
    public List<LikeWebtoonDTO> getLikedWebtoonsByUserId(Long userId) {
        try {
            AppUser user = authService.getUserByUserIdNotAdmin(userId); // ✅ 어드민 제외
            List<LikeWebtoonList> likedWebtoons = likeWebtoonListRepository.findLikedWebtoonsByUserId(user.getIndexId());

            return likedWebtoons.stream()
                    .map(like -> new LikeWebtoonDTO(
                            like.getWebtoon().getId(),
                            like.getWebtoon().getTitleName(),
                            like.getWebtoon().getThumbnailUrl()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("좋아요 한 웹툰을 불러올 수 없습니다.", "WEBTOON_ERROR");
        }
    }
}