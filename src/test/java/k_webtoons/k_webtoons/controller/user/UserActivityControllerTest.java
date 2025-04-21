package k_webtoons.k_webtoons.controller.user;

import k_webtoons.k_webtoons.model.user.userActivity.UserActivityInfoResponse;
import k_webtoons.k_webtoons.service.user.UserActivityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserActivityControllerTest {

    @Mock
    private UserActivityService userActivityService;

    @InjectMocks
    private UserActivityController userActivityController;

    @Test
    @DisplayName("프로필 이미지 업데이트 - 성공")
    void 프로필_이미지_업데이트_성공() {
        // Given
        MultipartFile mockFile = mock(MultipartFile.class);

        // When
        userActivityController.updateProfileImage(mockFile);

        // Then
        verify(userActivityService).updateProfileImage(mockFile);
    }

    @Test
    @DisplayName("자기소개 업데이트 - 성공")
    void 자기소개_업데이트_성공() {
        // Given
        String bio = "New bio";

        // When
        userActivityController.updateBio(new k_webtoons.k_webtoons.model.user.userActivity.BioUpdateRequest(bio));

        // Then
        verify(userActivityService).updateBio(bio);
    }

    @Test
    @DisplayName("프로필 공개 여부 업데이트 - 성공")
    void 프로필_공개_여부_업데이트_성공() {
        // Given
        boolean isPublic = true;

        // When
        userActivityController.updateProfileVisibility(
                new k_webtoons.k_webtoons.model.user.userActivity.ProfileVisibilityRequest(isPublic)
        );

        // Then
        verify(userActivityService).updateProfileVisibility(isPublic);
    }

    @Test
    @DisplayName("사용자 활동 정보 조회 - 성공")
    void 사용자_활동_정보_조회_성공() {
        // Given
        Long userId = 1L;
        UserActivityInfoResponse mockResponse = new UserActivityInfoResponse("image.jpg", "Bio");
        when(userActivityService.getUserActivityInfo(userId)).thenReturn(mockResponse);

        // When
        ResponseEntity<UserActivityInfoResponse> response = userActivityController.getUserActivityInfo(userId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("image.jpg", response.getBody().profileImageUrl());
    }

    @Test
    @DisplayName("프로필 이미지 URL 조회 - 성공")
    void 프로필_이미지_URL_조회_성공() {
        // Given
        Long userId = 1L;
        when(userActivityService.getProfileImageUrl(userId)).thenReturn("/img/image.jpg");

        // When
        ResponseEntity<Map<String, String>> response = userActivityController.getProfileImage(userId);

        // Then
        assertEquals("/img/image.jpg", response.getBody().get("profileImageUrl"));
    }

    @Test
    @DisplayName("자기소개 조회 - 성공")
    void 자기소개_조회_성공() {
        // Given
        Long userId = 1L;
        when(userActivityService.getBio(userId)).thenReturn("Sample Bio");

        // When
        ResponseEntity<String> response = userActivityController.getBio(userId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Sample Bio", response.getBody());
    }
}