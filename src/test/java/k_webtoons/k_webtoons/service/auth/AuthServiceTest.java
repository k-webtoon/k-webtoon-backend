package k_webtoons.k_webtoons.service.auth;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.auth.dto.*;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.service.user.UserActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserActivityService userActivityService;
    @InjectMocks private AuthService authService;

    private final String testEmail = "test@example.com";
    private final String testPhone = "010-1234-5678";

    // 회원가입 - 이메일 중복
    @Test
    void register_이메일중복_실패() {
        UserRegisterDTO dto = new UserRegisterDTO(
                testEmail, "pass", 20, "M", "nick",
                testPhone, "Q", "A"
        );
        when(userRepository.existsByUserEmail(testEmail)).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class,
                () -> authService.register(dto));
        assertEquals("EMAIL_ALREADY_IN_USE", exception.getErrorCode());
    }

    // 회원가입 - 정상
    @Test
    void register_정상() {
        UserRegisterDTO dto = new UserRegisterDTO(
                testEmail, "pass", 20, "M", "nick",
                testPhone, "Q", "A"
        );
        when(userRepository.existsByUserEmail(testEmail)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPw");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = authService.register(dto);

        assertEquals(testEmail, response.userEmail());
        assertEquals("nick", response.nickname());
        verify(userActivityService).createEmptyUserActivity(any());
    }

    // 전화번호로 보안질문 조회 - 성공
    @Test
    void getSecurityQuestionByPhoneNumber_성공() {
        AppUser user = new AppUser();
        user.setSecurityQuestion("질문");
        when(userRepository.findByPhoneNumber(testPhone)).thenReturn(Optional.of(user));

        String question = authService.getSecurityQuestionByPhoneNumber(new VerifyPhoneNumberDTO(testPhone));

        assertEquals("질문", question);
    }

    // 전화번호로 보안질문 조회 - 실패
    @Test
    void getSecurityQuestionByPhoneNumber_실패() {
        when(userRepository.findByPhoneNumber(testPhone)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> authService.getSecurityQuestionByPhoneNumber(new VerifyPhoneNumberDTO(testPhone)));
        assertEquals("PHONE_NUMBER_NOT_FOUND", exception.getErrorCode());
    }

    // 이메일로 보안질문 조회 - 성공
    @Test
    void getSecurityQuestionByEmail_성공() {
        AppUser user = new AppUser();
        user.setSecurityQuestion("질문2");
        when(userRepository.findByUserEmail(testEmail)).thenReturn(Optional.of(user));

        String question = authService.getSecurityQuestionByEmail(new VerifyEmailDTO(testEmail));

        assertEquals("질문2", question);
    }

    // 이메일로 보안질문 조회 - 실패
    @Test
    void getSecurityQuestionByEmail_실패() {
        when(userRepository.findByUserEmail(testEmail)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> authService.getSecurityQuestionByEmail(new VerifyEmailDTO(testEmail)));
        assertEquals("EMAIL_NOT_FOUND", exception.getErrorCode());
    }

    // 보안답변으로 이메일 찾기 - 성공
    @Test
    void findEmailBySecurityAnswer_성공() {
        AppUser user = new AppUser();
        user.setUserEmail(testEmail);
        user.setSecurityQuestion("Q");
        user.setSecurityAnswer("A");
        user.setPhoneNumber(testPhone);
        when(userRepository.findByPhoneNumber(testPhone)).thenReturn(Optional.of(user));

        String result = authService.findEmailBySecurityAnswer(new SecurityQuestionRequest(testPhone, "Q", "A"));

        assertEquals(testEmail, result);
    }

    // 보안답변으로 이메일 찾기 - 질문/답변 불일치
    @Test
    void findEmailBySecurityAnswer_질문답변불일치_실패() {
        AppUser user = new AppUser();
        user.setSecurityQuestion("Q");
        user.setSecurityAnswer("A");
        user.setPhoneNumber(testPhone);
        when(userRepository.findByPhoneNumber(testPhone)).thenReturn(Optional.of(user));

        CustomException exception = assertThrows(CustomException.class,
                () -> authService.findEmailBySecurityAnswer(new SecurityQuestionRequest(testPhone, "Q", "틀린답")));
        assertEquals("INVALID_SECURITY_ANSWER", exception.getErrorCode());
    }

    // 보안답변으로 이메일 찾기 - 전화번호 없음
    @Test
    void findEmailBySecurityAnswer_전화번호없음_실패() {
        when(userRepository.findByPhoneNumber(testPhone)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> authService.findEmailBySecurityAnswer(new SecurityQuestionRequest(testPhone, "Q", "A")));
        assertEquals("PHONE_NUMBER_NOT_FOUND", exception.getErrorCode());
    }

    // 보안답변으로 비밀번호 변경 - 성공
    @Test
    void changePassword_성공() {
        AppUser user = new AppUser();
        user.setSecurityQuestion("Q");
        user.setSecurityAnswer("A");
        user.setPhoneNumber(testPhone);
        when(userRepository.findByUserEmail(testEmail)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(any())).thenReturn("encrypted");

        ChangePasswordRequest request = new ChangePasswordRequest(testEmail, testPhone, "Q", "A", "newPass");
        authService.changePassword(request);

        verify(userRepository).save(user);
        assertEquals("encrypted", user.getUserPassword());
    }

    // 보안답변으로 비밀번호 변경 - 질문/답변/전화번호 불일치
    @Test
    void changePassword_조건불일치_실패() {
        AppUser user = new AppUser();
        user.setSecurityQuestion("Q");
        user.setSecurityAnswer("A");
        user.setPhoneNumber(testPhone);
        when(userRepository.findByUserEmail(testEmail)).thenReturn(Optional.of(user));

        ChangePasswordRequest request = new ChangePasswordRequest(testEmail, testPhone, "Q", "틀린답", "newPass");
        CustomException exception = assertThrows(CustomException.class,
                () -> authService.changePassword(request));
        assertEquals("INVALID_SECURITY_ANSWER", exception.getErrorCode());
    }

    // changePasswordWithCurrent - 현재 비밀번호 불일치
    @Test
    void changePasswordWithCurrent_현재비밀번호불일치_실패() {
        AppUser user = new AppUser();
        user.setUserPassword("encryptedCurrent");
        ChangePasswordWithCurrentRequest request = new ChangePasswordWithCurrentRequest(
                "wrongCurrent", "newPass", "newPass"
        );
        when(passwordEncoder.matches("wrongCurrent", "encryptedCurrent")).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class,
                () -> authService.changePasswordWithCurrent(user, request));
        assertEquals("INVALID_CURRENT_PASSWORD", exception.getErrorCode());
    }

    // changePasswordWithCurrent - 새 비밀번호 불일치
    @Test
    void changePasswordWithCurrent_새비밀번호불일치_실패() {
        AppUser user = new AppUser();
        user.setUserPassword("encryptedCurrent");
        ChangePasswordWithCurrentRequest request = new ChangePasswordWithCurrentRequest(
                "current", "newPass", "notMatch"
        );
        when(passwordEncoder.matches("current", "encryptedCurrent")).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class,
                () -> authService.changePasswordWithCurrent(user, request));
        assertEquals("PASSWORD_MISMATCH", exception.getErrorCode());
    }

    // changePasswordWithCurrent - 정상
    @Test
    void changePasswordWithCurrent_성공() {
        AppUser user = new AppUser();
        user.setUserPassword("encryptedCurrent");
        ChangePasswordWithCurrentRequest request = new ChangePasswordWithCurrentRequest(
                "current", "newPass", "newPass"
        );
        when(passwordEncoder.matches("current", "encryptedCurrent")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encryptedNew");

        authService.changePasswordWithCurrent(user, request);

        verify(userRepository).save(user);
        assertEquals("encryptedNew", user.getUserPassword());
    }
}