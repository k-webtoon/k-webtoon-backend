package k_webtoons.k_webtoons.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import k_webtoons.k_webtoons.model.user.userActivity.BioUpdateRequest;
import k_webtoons.k_webtoons.model.user.userActivity.ProfileVisibilityRequest;
import k_webtoons.k_webtoons.model.user.userActivity.UserActivityInfoResponse;
import k_webtoons.k_webtoons.service.user.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/user-activity")
@RequiredArgsConstructor
public class UserActivityController {

    private final UserActivityService userActivityService;

    @Operation(
            summary = "프로필 이미지 업데이트 API",
            description = "사용자의 프로필 이미지를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이미지 업데이트 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 이미지 형식", content = @Content)
            }
    )
    @PutMapping("/profile-image")
    public void updateProfileImage(
            @RequestParam("profileImage") MultipartFile profileImage
    ) {
        userActivityService.updateProfileImage(profileImage);
    }

    @Operation(
            summary = "자기소개 업데이트 API",
            description = "사용자의 자기소개를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "자기소개 업데이트 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content)
            }
    )
    @PutMapping("/bio")
    public void updateBio(@RequestBody BioUpdateRequest request) {
        userActivityService.updateBio(request.bio());
    }

    @Operation(
            summary = "프로필 공개 여부 업데이트 API",
            description = "사용자의 프로필 공개 여부를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "공개 여부 업데이트 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content)
            }
    )
    @PutMapping("/profile-visibility")
    public void updateProfileVisibility(@RequestBody ProfileVisibilityRequest request) {
        userActivityService.updateProfileVisibility(request.isProfilePublic());
    }

    @Operation(
            summary = "프로필 이미지 및 자기소개 조회 API",
            description = "사용자의 프로필 이미지 경로와 자기소개를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "사용자 활동 정보 없음", content = @Content)
            }
    )
    @GetMapping("/{userId}/info")
    public ResponseEntity<UserActivityInfoResponse> getUserActivityInfo(@PathVariable Long userId) {
        UserActivityInfoResponse response = userActivityService.getUserActivityInfo(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<Map<String, String>> getProfileImage(
            @PathVariable Long userId
    ) {
        String imageUrl = userActivityService.getProfileImageUrl(userId);
        return ResponseEntity.ok().body(Collections.singletonMap("profileImageUrl", imageUrl));
    }


    @Operation(
            summary = "자기소개 조회 API",
            description = "사용자의 자기소개를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(schema = @Schema(type = "string")) // 응답 타입 명시
                    ),
                    @ApiResponse(responseCode = "404", description = "사용자 활동 정보 없음", content = @Content)
            }
    )
    @GetMapping(value = "/{userId}/bio", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getBio(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userActivityService.getBio(userId));
    }
}