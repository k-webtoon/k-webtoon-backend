package k_webtoons.k_webtoons.service.auth;

import k_webtoons.k_webtoons.model.auth.*;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public UserResponse register(UserRegisterDTO dto) {
        if (userRepository.existsByUserEmail(dto.userEmail())) {
            throw new RuntimeException("이미 사용중인 이메일입니다.");
        }

        String role = "USER";

        AppUser newAppUser = new AppUser(
                dto.userEmail(),
                passwordEncoder.encode(dto.userPassword()),
                dto.userAge(),
                dto.gender(),
                dto.nickname(),
                role,
                dto.phoneNumber(),
                dto.securityQuestion(),
                dto.securityAnswer()
        );

        AppUser savedAppUser = userRepository.save(newAppUser);

        return new UserResponse(
                savedAppUser.getIndexId(),
                savedAppUser.getUserEmail(),
                savedAppUser.getNickname()
        );
    }

    // 전화번호로 보안질문 검색
    public String getSecurityQuestionByPhoneNumber(VerifyPhoneNumberDTO request) {
        try {
            AppUser user = userRepository.findByPhoneNumber(request.phoneNumber())
                    .orElseThrow(() -> new RuntimeException("전화번호가 없습니다."));
            return user.getSecurityQuestion();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // 보안답변으로 이메일 찾기
    public String findEmailBySecurityAnswer(SecurityQuestionRequest request) {
        try {
            AppUser user = userRepository.findByPhoneNumber(request.phoneNumber())
                    .orElseThrow(() -> new RuntimeException("전화번호가 없습니다."));

            if (user.getSecurityQuestion().equals(request.securityQuestion()) && user.getSecurityAnswer().equals(request.securityAnswer())) {
                return user.getUserEmail();
            } else {
                throw new RuntimeException("잘못된 답변입니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // 보안답변으로 비밀번호 변경
    public void changePassword(ChangePasswordRequest request) {
        try {
            AppUser user = userRepository.findByUserEmail(request.userEmail())
                    .orElseThrow(() -> new RuntimeException("이메일이 없습니다."));

            if (user.getPhoneNumber().equals(request.phoneNumber()) && user.getSecurityQuestion().equals(request.securityQuestion()) && user.getSecurityAnswer().equals(request.securityAnswer())) {
                user.setUserPassword(passwordEncoder.encode(request.newPassword()));
                userRepository.save(user);
            } else {
                throw new RuntimeException("잘못된 답변입니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public AppUser getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails)){
            throw  new RuntimeException("인증되지 않은 사용자입니다.");
        }

        String userEmail = ((UserDetails) principal).getUsername();
        return userRepository.findByUserEmail(userEmail).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // 여기서부턴 로직용 함수

    public AppUser getUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

}
