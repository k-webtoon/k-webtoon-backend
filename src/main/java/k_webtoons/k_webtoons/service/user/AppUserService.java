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

    // 사용자 정보 불러오기
    public UserInfoDTO getUserInfo() {
        try {
            AppUser user = authService.getAuthenticatedUser();
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

    public UserInfoDTO getUserInfoByUserId(Long userId) {
        try {
            AppUser user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", "USER_NOT_FOUND"));

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

    public List<UserCommentResponseDTO> getCommentsByUserId(Long userId) {
        try {
            List<WebtoonComment> comments = webtoonCommentRepository.findByUserIdAndDeletedDateTimeIsNull(userId);

            if (comments.isEmpty()) {
                return List.of(); // 빈 리스트 반환
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
            e.printStackTrace(); // 예외 출력
            throw new CustomException("댓글을 불러올 수 없습니다.", "COMMENT_ERROR");
        }
    }

    public List<LikeWebtoonDTO> getLikedWebtoonsByUserId(Long userId) {
        try {
            // JPQL을 사용해 좋아요한 웹툰 목록 조회
            List<LikeWebtoonList> likedWebtoons = likeWebtoonListRepository.findLikedWebtoonsByUserId(userId);

            // DTO로 변환하여 반환
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
