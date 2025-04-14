package k_webtoons.k_webtoons.service.auth;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.*;
import k_webtoons.k_webtoons.model.auth.dto.*;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.service.user.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserActivityService userActivityService;

    // 회원가입
    @Transactional
    public UserResponse register(UserRegisterDTO dto) {
        if (userRepository.existsByUserEmail(dto.userEmail())) {
            throw new CustomException("이미 사용 중인 이메일입니다.", "EMAIL_ALREADY_IN_USE");
        }

        String role = "USER";

        try {
            AppUser newAppUser = new AppUser(
                    dto.userEmail(),
                    passwordEncoder.encode(dto.userPassword()),
                    dto.userAge(),
                    dto.gender(),
                    dto.nickname(),
                    role,
                    dto.phoneNumber(),
                    dto.securityQuestion(),
                    dto.securityAnswer(),
                    LocalDateTime.now(),
                    "LOCAL"
            );

            AppUser savedAppUser = userRepository.save(newAppUser);

            userActivityService.createEmptyUserActivity(savedAppUser);

            return new UserResponse(
                    savedAppUser.getIndexId(),
                    savedAppUser.getUserEmail(),
                    savedAppUser.getNickname()
            );
        } catch (Exception e) {
            throw new CustomException("회원가입 중 오류가 발생했습니다: " + e.getMessage(), "REGISTRATION_FAILED");
        }
    }

    // 전화번호로 보안질문 검색
    @Transactional(readOnly = true)
    public String getSecurityQuestionByPhoneNumber(VerifyPhoneNumberDTO request) {
        return userRepository.findByPhoneNumber(request.phoneNumber())
                .map(AppUser::getSecurityQuestion)
                .orElseThrow(() -> new CustomException("해당 전화번호를 사용하는 사용자가 없습니다.", "PHONE_NUMBER_NOT_FOUND"));
    }

    // 이메일로 보안질문 검색
    @Transactional(readOnly = true)
    public String getSecurityQuestionByEmail(VerifyEmailDTO request) {
        return userRepository.findByUserEmail(request.email())
                .map(AppUser::getSecurityQuestion)
                .orElseThrow(() -> new CustomException("해당 이메일을 사용하는 사용자가 없습니다.", "EMAIL_NOT_FOUND"));
    }

    // 보안답변으로 이메일 찾기
    @Transactional(readOnly = true)
    public String findEmailBySecurityAnswer(SecurityQuestionRequest request) {
        AppUser user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new CustomException("해당 전화번호를 사용하는 사용자가 없습니다.", "PHONE_NUMBER_NOT_FOUND"));

        if (!user.getSecurityQuestion().equals(request.securityQuestion()) || !user.getSecurityAnswer().equals(request.securityAnswer())) {
            throw new CustomException("보안 질문 또는 답변이 올바르지 않습니다.", "INVALID_SECURITY_ANSWER");
        }

        return user.getUserEmail();
    }

    // 보안답변으로 비밀번호 변경
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        AppUser user = userRepository.findByUserEmail(request.userEmail())
                .orElseThrow(() -> new CustomException("해당 이메일을 사용하는 사용자가 없습니다.", "EMAIL_NOT_FOUND"));

        if (!user.getPhoneNumber().equals(request.phoneNumber()) ||
                !user.getSecurityQuestion().equals(request.securityQuestion()) ||
                !user.getSecurityAnswer().equals(request.securityAnswer())) {
            throw new CustomException("보안 질문 또는 답변이 올바르지 않습니다.", "INVALID_SECURITY_ANSWER");
        }

        try {
            user.setUserPassword(passwordEncoder.encode(request.newPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            throw new CustomException("비밀번호 변경 중 오류가 발생했습니다: " + e.getMessage(), "PASSWORD_CHANGE_FAILED");
        }
    }

    // 인증된 사용자 가져오기
    public AppUser getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails)) {
            throw new CustomException("인증되지 않은 사용자입니다.", "UNAUTHENTICATED_USER");
        }

        String userEmail = ((UserDetails) principal).getUsername();
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", "USER_NOT_FOUND"));
    }

    // 사용자 ID로 사용자 조회
    public AppUser getUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", "USER_NOT_FOUND"));
    }

    // 특정 사용자 조회 (어드민 제외)
    public AppUser getUserByUserIdNotAdmin(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", "USER_NOT_FOUND"));

        // 어드민 계정은 조회 불가능
//        if ("ADMIN".equals(user.getRole())) {
//            throw new CustomException("어드민 계정을 조회할 수 없습니다.", "ADMIN_ACCESS_DENIED");
//        }

        return user;
    }

    @Transactional
    public void changePasswordWithCurrent(AppUser user, ChangePasswordWithCurrentRequest request) {
        // 1. 현재 비밀번호 검증
        if (!passwordEncoder.matches(request.currentPassword(), user.getUserPassword())) {
            throw new CustomException("현재 비밀번호가 일치하지 않습니다.", "INVALID_CURRENT_PASSWORD");
        }

        // 2. 새 비밀번호 일치 확인
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new CustomException("새 비밀번호와 확인용 비밀번호가 일치하지 않습니다.", "PASSWORD_MISMATCH");
        }

        // 3. 비밀번호 업데이트
        try {
            user.setUserPassword(passwordEncoder.encode(request.newPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            throw new CustomException("비밀번호 변경 중 오류 발생: " + e.getMessage(), "PASSWORD_UPDATE_FAILED");
        }
    }

}
