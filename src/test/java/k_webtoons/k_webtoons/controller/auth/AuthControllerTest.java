package k_webtoons.k_webtoons.controller.auth;

import k_webtoons.k_webtoons.controller.auth.AuthController;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.auth.dto.*;
import k_webtoons.k_webtoons.security.AppUserDetails;
import k_webtoons.k_webtoons.security.HeaderValidator;
import k_webtoons.k_webtoons.security.JwtUtil;
import k_webtoons.k_webtoons.service.auth.AuthService;
import k_webtoons.k_webtoons.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock private AuthService authService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtUtil jwtUtil;
    @Mock private HeaderValidator headerValidator;
    @InjectMocks private AuthController authController;

    private final String testToken = "test.jwt.token";
    private final String testEmail = "test@example.com";
    private final String testPhone = "010-1234-5678";

    // 회원가입 성공 케이스
    @Test
    @DisplayName("회원가입 성공 케이스")
    void register_성공() {
        UserRegisterDTO dto = new UserRegisterDTO(
                testEmail, "password", 20, "M", "nickname",
                testPhone, "질문", "답변"
        );
        UserResponse response = new UserResponse(1L, testEmail, "nickname");
        when(authService.register(dto)).thenReturn(response);

        ResponseEntity<UserResponse> result = authController.register(dto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    // 회원가입 실패(이메일 중복) 케이스
    @Test
    @DisplayName("회원가입 실패(이메일 중복) 케이스")
    void register_이메일중복_실패() {
        UserRegisterDTO dto = new UserRegisterDTO(
                testEmail, "password", 20, "M", "nickname",
                testPhone, "질문", "답변"
        );
        when(authService.register(dto)).thenThrow(new CustomException("이미 사용 중인 이메일입니다.", "EMAIL_ALREADY_IN_USE"));

        CustomException ex = assertThrows(CustomException.class, () -> authController.register(dto));
        assertEquals("EMAIL_ALREADY_IN_USE", ex.getErrorCode());
    }

    // 로그인 성공 케이스
    @Test
    @DisplayName("로그인 성공 케이스")
    void login_성공() {
        LoginDTO dto = new LoginDTO(testEmail, "password");
        Authentication authentication = mock(Authentication.class);
        AppUserDetails userDetails = new AppUserDetails(
                new AppUser(testEmail, "encPass", 20, "M", "nick", "USER", testPhone, "Q", "A", null, "LOCAL")
        );
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(any(), any(), any())).thenReturn(testToken);

        ResponseEntity<String> result = authController.login(dto);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(testToken, result.getBody());
    }

    // 로그인 실패(비밀번호 틀림) 케이스 (AuthenticationManager가 예외 throw)
    @Test
    @DisplayName("로그인 실패(비밀번호 틀림) 케이스")
    void login_비밀번호틀림_실패() {
        LoginDTO dto = new LoginDTO(testEmail, "wrongpw");
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Bad credentials"));

        assertThrows(RuntimeException.class, () -> authController.login(dto));
    }

    // 전화번호 인증 성공
    @Test
    @DisplayName("전화번호 인증 성공")
    void verifyPhoneNumber_성공() {
        VerifyPhoneNumberDTO dto = new VerifyPhoneNumberDTO(testPhone);
        when(authService.getSecurityQuestionByPhoneNumber(dto)).thenReturn("질문");

        ResponseEntity<String> result = authController.verifyPhoneNumber(dto);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("질문", result.getBody());
    }

    // 전화번호 인증 실패(존재하지 않는 번호)
    @Test
    @DisplayName("전화번호 인증 실패")
    void verifyPhoneNumber_존재하지않음_실패() {
        VerifyPhoneNumberDTO dto = new VerifyPhoneNumberDTO(testPhone);
        when(authService.getSecurityQuestionByPhoneNumber(dto)).thenThrow(new CustomException("해당 전화번호를 사용하는 사용자가 없습니다.", "PHONE_NUMBER_NOT_FOUND"));

        CustomException ex = assertThrows(CustomException.class, () -> authController.verifyPhoneNumber(dto));
        assertEquals("PHONE_NUMBER_NOT_FOUND", ex.getErrorCode());
    }

    // 이메일 인증 성공
    @Test
    @DisplayName("이메일 인증 성공")
    void verifyEmail_성공() {
        VerifyEmailDTO dto = new VerifyEmailDTO(testEmail);
        when(authService.getSecurityQuestionByEmail(dto)).thenReturn("질문2");

        ResponseEntity<String> result = authController.verifyEmail(dto);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("질문2", result.getBody());
    }

    // 이메일 인증 실패(존재하지 않는 이메일)
    @Test
    @DisplayName("이메일 인증 실패")
    void verifyEmail_존재하지않음_실패() {
        VerifyEmailDTO dto = new VerifyEmailDTO(testEmail);
        when(authService.getSecurityQuestionByEmail(dto)).thenThrow(new CustomException("해당 이메일을 사용하는 사용자가 없습니다.", "EMAIL_NOT_FOUND"));

        CustomException ex = assertThrows(CustomException.class, () -> authController.verifyEmail(dto));
        assertEquals("EMAIL_NOT_FOUND", ex.getErrorCode());
    }

    // 이메일 찾기 성공
    @Test
    @DisplayName("이메일 찾기 성공")
    void findEmail_성공() {
        SecurityQuestionRequest dto = new SecurityQuestionRequest(testPhone, "Q", "A");
        when(authService.findEmailBySecurityAnswer(dto)).thenReturn(testEmail);

        ResponseEntity<String> result = authController.findEmail(dto);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(testEmail, result.getBody());
    }

    // 이메일 찾기 실패(보안답변 불일치)
    @Test
    @DisplayName("이메일 찾기 실패")
    void findEmail_보안답변불일치_실패() {
        SecurityQuestionRequest dto = new SecurityQuestionRequest(testPhone, "Q", "틀린답");
        when(authService.findEmailBySecurityAnswer(dto)).thenThrow(new CustomException("보안 질문 또는 답변이 올바르지 않습니다.", "INVALID_SECURITY_ANSWER"));

        CustomException ex = assertThrows(CustomException.class, () -> authController.findEmail(dto));
        assertEquals("INVALID_SECURITY_ANSWER", ex.getErrorCode());
    }

    // 보안질문으로 비밀번호 변경 성공
    @Test
    @DisplayName("보안질문으로 비밀번호 변경 성공")
    void changePassword_성공() {
        ChangePasswordRequest dto = new ChangePasswordRequest(testEmail, testPhone, "Q", "A", "newPass");
        // void 메소드라서 doNothing()이 기본값

        ResponseEntity<String> result = authController.changePassword(dto);

        verify(authService).changePassword(dto);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Password changed successfully", result.getBody());
    }

    // 보안질문으로 비밀번호 변경 실패(조건 불일치)
    @Test
    @DisplayName("보안질문으로 비밀번호 변경 실패")
    void changePassword_조건불일치_실패() {
        ChangePasswordRequest dto = new ChangePasswordRequest(testEmail, testPhone, "Q", "틀린답", "newPass");
        doThrow(new CustomException("보안 질문 또는 답변이 올바르지 않습니다.", "INVALID_SECURITY_ANSWER"))
                .when(authService).changePassword(dto);

        CustomException ex = assertThrows(CustomException.class, () -> authController.changePassword(dto));
        assertEquals("INVALID_SECURITY_ANSWER", ex.getErrorCode());
    }

    // 현재 비밀번호로 비밀번호 변경 성공
    @Test
    @DisplayName("현재 비밀번호로 비밀번호 변경 성공")
    void changePasswordWithCurrent_성공() {
        ChangePasswordWithCurrentRequest request = new ChangePasswordWithCurrentRequest(
                "currentPass", "newPass", "newPass"
        );
        AppUser mockUser = new AppUser();
        when(headerValidator.getAuthenticatedUser()).thenReturn(mockUser);

        ResponseEntity<String> result = authController.changePasswordWithCurrent(request);

        verify(authService).changePasswordWithCurrent(mockUser, request);
        assertEquals("비밀번호가 성공적으로 변경되었습니다.", result.getBody());
    }

    // 현재 비밀번호로 비밀번호 변경 실패(현재 비밀번호 불일치)
    @Test
    @DisplayName("현재 비밀번호로 비밀번호 변경 실패")
    void changePasswordWithCurrent_현재비밀번호불일치_실패() {
        ChangePasswordWithCurrentRequest request = new ChangePasswordWithCurrentRequest(
                "wrongCurrent", "newPass", "newPass"
        );
        AppUser mockUser = new AppUser();
        when(headerValidator.getAuthenticatedUser()).thenReturn(mockUser);
        doThrow(new CustomException("현재 비밀번호가 일치하지 않습니다.", "INVALID_CURRENT_PASSWORD"))
                .when(authService).changePasswordWithCurrent(mockUser, request);

        CustomException ex = assertThrows(CustomException.class, () -> authController.changePasswordWithCurrent(request));
        assertEquals("INVALID_CURRENT_PASSWORD", ex.getErrorCode());
    }
}