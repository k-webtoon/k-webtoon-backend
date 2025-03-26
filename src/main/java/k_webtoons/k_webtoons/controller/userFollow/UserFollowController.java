package k_webtoons.k_webtoons.controller.userFollow;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import k_webtoons.k_webtoons.model.user.AppUser;
import k_webtoons.k_webtoons.service.user.UserFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
@Tag(name = "User Follow", description = "유저 팔로우 / 언팔로우 관련 API")
public class UserFollowController {

    private final UserFollowService userFollowService;

    @Operation(summary = "팔로우", description = "followerId 사용자가 followeeId 사용자를 팔로우합니다.")
    @PostMapping("/{followerId}/follow/{followeeId}")
    public ResponseEntity<Void> follow(
            @Parameter(description = "팔로우하는 유저 ID", example = "1") @PathVariable int followerId,
            @Parameter(description = "팔로우 당하는 유저 ID", example = "2") @PathVariable int followeeId
    ) {
        userFollowService.follow(followerId, followeeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "언팔로우", description = "followerId 사용자가 followeeId 사용자를 언팔로우합니다.")
    @DeleteMapping("/{followerId}/unfollow/{followeeId}")
    public ResponseEntity<Void> unfollow(
            @Parameter(description = "언팔로우하는 유저 ID", example = "1") @PathVariable int followerId,
            @Parameter(description = "언팔로우 당하는 유저 ID", example = "2") @PathVariable int followeeId
    ) {
        userFollowService.unfollow(followerId, followeeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "팔로워 리스트 조회", description = "userId 사용자를 팔로우하는 유저들의 리스트를 반환합니다.")
    @GetMapping("/{userId}/followers")
    public List<AppUser> getFollowers(
            @Parameter(description = "팔로워를 조회할 대상 유저 ID", example = "1") @PathVariable long userId
    ) {
        return userFollowService.getFollowers(userId);
    }

    @Operation(summary = "팔로잉 리스트 조회", description = "userId 사용자가 팔로우하고 있는 유저들의 리스트를 반환합니다.")
    @GetMapping("/{userId}/followees")
    public List<AppUser> getFollowees(
            @Parameter(description = "팔로잉을 조회할 대상 유저 ID", example = "1") @PathVariable long userId
    ) {
        return userFollowService.getFollowees(userId);
    }

    @Operation(summary = "팔로워 수 조회", description = "userId 사용자의 팔로워 수를 반환합니다.")
    @GetMapping("/{userId}/followers/count")
    public long getFollowerCount(
            @Parameter(description = "팔로워 수를 조회할 유저 ID", example = "1") @PathVariable long userId
    ) {
        return userFollowService.getFollowerCount(userId);
    }

    @Operation(summary = "팔로잉 수 조회", description = "userId 사용자가 팔로우한 유저 수를 반환합니다.")
    @GetMapping("/{userId}/followees/count")
    public long getFolloweeCount(
            @Parameter(description = "팔로잉 수를 조회할 유저 ID", example = "1") @PathVariable long userId
    ) {
        return userFollowService.getFolloweeCount(userId);
    }
}
