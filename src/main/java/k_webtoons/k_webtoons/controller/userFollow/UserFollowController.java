package k_webtoons.k_webtoons.controller.userFollow;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user_follow.FollowUserDTO;
import k_webtoons.k_webtoons.service.user.UserFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
@Tag(name = "User Follow", description = "유저 팔로우 / 언팔로우 및 조회 관련 API")
public class UserFollowController {

    private final UserFollowService userFollowService;

    // 현재 인증된 사용자가 특정 사용자를 팔로우
    @Operation(summary = "팔로우", description = "현재 인증된 사용자가 followeeId 사용자를 팔로우합니다.")
    @PostMapping("/{followeeId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> follow(
            @Parameter(description = "팔로우 당하는 유저 ID", example = "2") @PathVariable long followeeId
    ) {
        userFollowService.follow(followeeId);
        return ResponseEntity.ok().build();
    }

    // 현재 인증된 사용자가 특정 사용자를 언팔로우
    @Operation(summary = "언팔로우", description = "현재 인증된 사용자가 followeeId 사용자를 언팔로우합니다.")
    @DeleteMapping("/{followeeId}/unfollow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unfollow(
            @Parameter(description = "언팔로우 당하는 유저 ID", example = "2") @PathVariable long followeeId
    ) {
        userFollowService.unfollow(followeeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용자의 팔로워 목록 조회", description = "{userId} 사용자의 팔로워 목록을 반환합니다.")
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowUserDTO>> getFollowers(
            @Parameter(description = "조회할 유저 ID", example = "1") @PathVariable long userId
    ) {
        List<FollowUserDTO> followers = userFollowService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @Operation(summary = "사용자의 팔로잉 목록 조회", description = "{userId} 사용자가 팔로우한 유저 목록을 반환합니다.")
    @GetMapping("/{userId}/followees")
    public ResponseEntity<List<FollowUserDTO>> getFollowees(
            @Parameter(description = "조회할 유저 ID", example = "1") @PathVariable long userId
    ) {
        List<FollowUserDTO> followees = userFollowService.getFollowees(userId);
        return ResponseEntity.ok(followees);
    }


    // 특정 사용자의 팔로워 수 조회
    @Operation(summary = "{userId}의 팔로워 수 조회", description = "{userId} 사용자의 팔로워 수를 반환합니다.")
    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Long> getFollowerCount(
            @Parameter(description = "조회할 유저 ID", example = "1") @PathVariable long userId
    ) {
        long followerCount = userFollowService.getFollowerCount(userId);
        return ResponseEntity.ok(followerCount);
    }

    // 특정 사용자의 팔로잉 수 조회
    @Operation(summary = "{userId}의 팔로잉 수 조회", description = "{userId} 사용자가 팔로우한 유저 수를 반환합니다.")
    @GetMapping("/{userId}/followees/count")
    public ResponseEntity<Long> getFolloweeCount(
            @Parameter(description = "조회할 유저 ID", example = "1") @PathVariable long userId
    ) {
        long followeeCount = userFollowService.getFolloweeCount(userId);
        return ResponseEntity.ok(followeeCount);
    }
}
