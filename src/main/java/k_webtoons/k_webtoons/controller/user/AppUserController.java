package k_webtoons.k_webtoons.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user.FollowAppUserDTO;
import k_webtoons.k_webtoons.model.user.LikeWebtoonDTO;
import k_webtoons.k_webtoons.model.user.UserCommentResponseDTO;
import k_webtoons.k_webtoons.model.user.UserInfoDTO;
import k_webtoons.k_webtoons.service.user.AppUserService;
import k_webtoons.k_webtoons.service.user.UserFollowService;
import k_webtoons.k_webtoons.service.webtoon.LikeWebtoonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService userService;
    private final UserFollowService userFollowService;

    @Operation(
            summary = "사용자 정보 조회 API",
            description = "특정 사용자의 정보를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "사용자 정보 성공적으로 반환",
                            content = @Content(schema = @Schema(implementation = UserInfoDTO.class))
                    )
            }
    )
    @GetMapping("/{userId}/info")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserInfoByUserId(userId));
    }

    @Operation(
            summary = "특정 사용자가 작성한 댓글 조회 API",
            description = "특정 사용자가 작성한 댓글 목록을 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 목록 성공적으로 반환",
                            content = @Content(schema = @Schema(implementation = UserCommentResponseDTO.class))
                    )
            }
    )
    @GetMapping("/{userId}/comments")
    public ResponseEntity<List<UserCommentResponseDTO>> getComments(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getCommentsByUserId(userId));
    }

    @Operation(
            summary = "특정 사용자가 좋아요 한 웹툰 조회 API",
            description = "특정 사용자가 좋아요 한 웹툰 목록을 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "웹툰 목록 성공적으로 반환",
                            content = @Content(schema = @Schema(implementation = LikeWebtoonDTO.class))
                    )
            }
    )
    @GetMapping("/{userId}/liked-webtoons")
    public ResponseEntity<List<LikeWebtoonDTO>> getLikedWebtoonsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getLikedWebtoonsByUserId(userId));
    }

    @Operation(
            summary = "특정 사용자가 팔로우하는 사용자 목록 조회 API",
            description = "특정 사용자가 팔로우하는 사용자 목록을 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "팔로잉 목록 성공적으로 반환",
                            content = @Content(schema = @Schema(implementation = FollowAppUserDTO.class))
                    )
            }
    )
    @GetMapping("/{userId}/followees")
    public ResponseEntity<List<FollowAppUserDTO>> getFollowees(@PathVariable Long userId) {
        List<AppUser> followees = userFollowService.getFollowees(userId);
        return ResponseEntity.ok(followees.stream()
                .map(followee -> new FollowAppUserDTO(
                        followee.getIndexId(),
                        followee.getUserEmail(),
                        followee.getNickname()
                ))
                .collect(Collectors.toList()));
    }

    @Operation(
            summary = "특정 사용자를 팔로우하는 사용자 목록 조회 API",
            description = "특정 사용자를 팔로우하는 사용자 목록을 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "팔로워 목록 성공적으로 반환",
                            content = @Content(schema = @Schema(implementation = FollowAppUserDTO.class))
                    )
            }
    )
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowAppUserDTO>> getFollowers(@PathVariable Long userId) {
        List<AppUser> followers = userFollowService.getFollowers(userId);
        return ResponseEntity.ok(followers.stream()
                .map(follower -> new FollowAppUserDTO(
                        follower.getIndexId(),
                        follower.getUserEmail(),
                        follower.getNickname()
                ))
                .collect(Collectors.toList()));
    }
}