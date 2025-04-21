package k_webtoons.k_webtoons.service.user;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user.userActivity.UserActivity;
import k_webtoons.k_webtoons.model.user.userActivity.UserActivityInfoResponse;
import k_webtoons.k_webtoons.repository.user.UserActivityRepository;
import k_webtoons.k_webtoons.security.HeaderValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = "upload.image.path=/tmp")
class UserActivityServiceTest {

    @Mock
    private UserActivityRepository userActivityRepository;

    @Mock
    private HeaderValidator headerValidator;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private UserActivityService userActivityService;

    @Test
    @DisplayName("프로필 이미지 업데이트 - 성공")
    void 프로필_이미지_업데이트_성공() throws Exception {
        // 리플렉션을 이용해 private 필드에 임시 경로 주입
        Field uploadPathField = UserActivityService.class.getDeclaredField("uploadImagePath");
        uploadPathField.setAccessible(true);
        uploadPathField.set(userActivityService, System.getProperty("java.io.tmpdir"));

        // Given
        AppUser user = new AppUser();
        user.setIndexId(1L);
        when(headerValidator.getAuthenticatedUser()).thenReturn(user);

        UserActivity activity = new UserActivity();
        when(userActivityRepository.findByAppUser(user)).thenReturn(Optional.of(activity));
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");

        // When
        userActivityService.updateProfileImage(multipartFile);

        // Then
        assertNotNull(activity.getProfileImageUrl());
        verify(userActivityRepository).save(activity);
    }

    @Test
    @DisplayName("프로필 이미지 업데이트 - 사용자 없음")
    void 프로필_이미지_업데이트_사용자_없음() {
        when(headerValidator.getAuthenticatedUser()).thenThrow(new CustomException("인증 실패", "AUTH_ERROR"));

        assertThrows(CustomException.class, () ->
                userActivityService.updateProfileImage(multipartFile));
    }

    @Test
    @DisplayName("자기소개 업데이트 - 성공")
    void 자기소개_업데이트_성공() {
        // Given
        AppUser user = new AppUser();
        when(headerValidator.getAuthenticatedUser()).thenReturn(user);

        UserActivity activity = new UserActivity();
        when(userActivityRepository.findByAppUser(user)).thenReturn(Optional.of(activity));

        // When
        userActivityService.updateBio("New Bio");

        // Then
        assertEquals("New Bio", activity.getBio());
        verify(userActivityRepository).save(activity);
    }

    @Test
    @DisplayName("프로필 공개 여부 업데이트 - 성공")
    void 프로필_공개_여부_업데이트_성공() {
        // Given
        AppUser user = new AppUser();
        when(headerValidator.getAuthenticatedUser()).thenReturn(user);

        UserActivity activity = new UserActivity();
        when(userActivityRepository.findByAppUser(user)).thenReturn(Optional.of(activity));

        // When
        userActivityService.updateProfileVisibility(true);

        // Then
        assertTrue(activity.getIsProfilePublic());
        verify(userActivityRepository).save(activity);
    }

    @Test
    @DisplayName("사용자 활동 정보 조회 - 성공")
    void 사용자_활동_정보_조회_성공() {
        // Given
        Long userId = 1L;
        UserActivity activity = new UserActivity();
        activity.setProfileImageUrl("image.jpg");
        activity.setBio("Bio");
        when(userActivityRepository.findByUserId(userId)).thenReturn(Optional.of(activity));

        // When
        UserActivityInfoResponse response = userActivityService.getUserActivityInfo(userId);

        // Then
        assertEquals("image.jpg", response.profileImageUrl());
        assertEquals("Bio", response.bio());
    }

    @Test
    @DisplayName("프로필 이미지 URL 조회 - 성공")
    void 프로필_이미지_URL_조회_성공() {
        // Given
        Long userId = 1L;
        UserActivity activity = new UserActivity();
        activity.setProfileImageUrl("image.jpg");
        when(userActivityRepository.findByUserId(userId)).thenReturn(Optional.of(activity));

        // When
        String imageUrl = userActivityService.getProfileImageUrl(userId);

        // Then
        assertEquals("/img/image.jpg", imageUrl);
    }
}