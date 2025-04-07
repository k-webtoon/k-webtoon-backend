package k_webtoons.k_webtoons.service.user;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user.userActivity.UserActivity;
import k_webtoons.k_webtoons.model.user.userActivity.UserActivityCreateDTO;
import k_webtoons.k_webtoons.repository.user.UserActivityRepository;
import k_webtoons.k_webtoons.security.HeaderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;
    private final HeaderValidator headerValidator;

    // UserActivity 생성 메서드
    private UserActivity createUserActivity(AppUser appUser, UserActivityCreateDTO dto) {
        if (appUser == null) {
            throw new CustomException("사용자 정보가 없습니다.", "USER_ACTIVITY_INVALID_USER");
        }

        try {
            UserActivity userActivity = new UserActivity();
            userActivity.setProfileImage(dto.profileImage());
            userActivity.setBio(dto.bio());
            userActivity.setIsProfilePublic(dto.isProfilePublic());
            userActivity.setAppUser(appUser);

            return userActivityRepository.save(userActivity);
        } catch (Exception e) {
            throw new CustomException("활동 정보 생성 중 오류가 발생했습니다: " + e.getMessage(), "USER_ACTIVITY_CREATION_FAILED");
        }
    }

    // 사용자 프로필 업데이트 메서드
    public void updateUserActivity(MultipartFile profileImage, String bio, Boolean isProfilePublic) {
        // 인증된 사용자 가져오기
        AppUser authenticatedUser = headerValidator.getAuthenticatedUser();

        // 사용자 활동 정보 가져오기
        UserActivity userActivity = userActivityRepository.findByAppUser(authenticatedUser)
                .orElseThrow(() -> new CustomException("사용자의 활동 정보를 찾을 수 없습니다.", "USER_ACTIVITY_NOT_FOUND"));

        try {
            // 프로필 이미지가 제공된 경우 업데이트
            if (profileImage != null && !profileImage.isEmpty()) {
                userActivity.setProfileImage(profileImage.getBytes());
            }

            // 자기소개 및 공개 여부 업데이트
            if (bio != null) {
                userActivity.setBio(bio);
            }
            if (isProfilePublic != null) {
                userActivity.setIsProfilePublic(isProfilePublic);
            }

            // 저장소에 업데이트된 정보 저장
            userActivityRepository.save(userActivity);

        } catch (IOException e) {
            throw new CustomException("프로필 이미지를 처리하는 중 오류가 발생했습니다: " + e.getMessage(), "USER_ACTIVITY_IMAGE_PROCESSING_FAILED");
        }
    }


    // 빈 UserActivity 생성 메서드 (기본값 사용)
    public void createEmptyUserActivity(AppUser appUser) {
        if (appUser == null) {
            throw new CustomException("사용자 정보가 없습니다.", "USER_ACTIVITY_INVALID_USER");
        }

        try {
            UserActivityCreateDTO emptyDto = new UserActivityCreateDTO(null, null, false); // 기본값 DTO 생성
            createUserActivity(appUser, emptyDto); // DTO 기반으로 엔티티 생성
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("빈 활동 정보 생성 중 오류가 발생했습니다: " + e.getMessage(), "USER_ACTIVITY_CREATION_FAILED");
        }
    }
}
