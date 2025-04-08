package k_webtoons.k_webtoons.service.user;

import k_webtoons.k_webtoons.config.FileUtils;
import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user.userActivity.UserActivity;
import k_webtoons.k_webtoons.model.user.userActivity.UserActivityCreateDTO;
import k_webtoons.k_webtoons.model.user.userActivity.UserActivityInfoResponse;
import k_webtoons.k_webtoons.repository.user.UserActivityRepository;
import k_webtoons.k_webtoons.security.HeaderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;
    private final HeaderValidator headerValidator;

    @Value("${upload.image.path}")
    private String uploadImagePath;

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

    // UserActivity 생성 메서드
    private UserActivity createUserActivity(AppUser appUser, UserActivityCreateDTO dto) {
        if (appUser == null) {
            throw new CustomException("사용자 정보가 없습니다.", "USER_ACTIVITY_INVALID_USER");
        }

        try {
            UserActivity userActivity = new UserActivity();
            userActivity.setProfileImagePath(dto.profileImagePath()); // ✅ 이미지 경로 저장 방식으로 변경
            userActivity.setBio(dto.bio());
            userActivity.setIsProfilePublic(dto.isProfilePublic());
            userActivity.setAppUser(appUser);

            return userActivityRepository.save(userActivity);
        } catch (Exception e) {
            throw new CustomException("활동 정보 생성 중 오류가 발생했습니다: " + e.getMessage(), "USER_ACTIVITY_CREATION_FAILED");
        }
    }

    // 프로필 이미지 업데이트
    @Transactional
    public void updateProfileImage(MultipartFile profileImage) {
        AppUser authenticatedUser = headerValidator.getAuthenticatedUser();
        UserActivity activity = getUserActivity(authenticatedUser);

        try {
            // 파일명 해시화 (중복 방지)
            String hashedFileName = FileUtils.generateHashedFileName(
                    authenticatedUser.getIndexId() +
                            profileImage.getOriginalFilename() +
                            System.currentTimeMillis()
            );

            // 경로 생성
            Path uploadDir = Paths.get(uploadImagePath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 파일 저장
            Path targetPath = uploadDir.resolve(hashedFileName);
            profileImage.transferTo(targetPath);

            // DB에 절대 경로 저장
            activity.setProfileImagePath(targetPath.toString());
            userActivityRepository.save(activity);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new CustomException("이미지 처리 실패: " + e.getMessage(), "IMAGE_PROCESS_ERROR");
        }
    }

    // 자기소개 업데이트
    public void updateBio(String bio) {
        AppUser authenticatedUser = headerValidator.getAuthenticatedUser();
        UserActivity activity = getUserActivity(authenticatedUser);
        activity.setBio(bio);
        userActivityRepository.save(activity);
    }

    // 프로필 공개 여부 업데이트
    public void updateProfileVisibility(Boolean isProfilePublic) {
        AppUser authenticatedUser = headerValidator.getAuthenticatedUser();
        UserActivity activity = getUserActivity(authenticatedUser);
        activity.setIsProfilePublic(isProfilePublic);
        userActivityRepository.save(activity);
    }

    // 공통 사용자 활동 정보 조회 (HeaderValidator 사용)
    private UserActivity getUserActivity(AppUser authenticatedUser) {
        return userActivityRepository.findByAppUser(authenticatedUser)
                .orElseThrow(() -> new CustomException("활동 정보 없음", "USER_ACTIVITY_NOT_FOUND"));
    }

    public UserActivityInfoResponse getUserActivityInfo(Long userId) {
        UserActivity userActivity = userActivityRepository.findByAppUser_indexId(userId)
                .orElseThrow(() -> new CustomException("사용자 활동 정보를 찾을 수 없습니다.", "USER_ACTIVITY_NOT_FOUND"));

        return new UserActivityInfoResponse(
                userActivity.getProfileImagePath(),
                userActivity.getBio()
        );
    }

}
