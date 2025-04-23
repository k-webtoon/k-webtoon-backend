package k_webtoons.k_webtoons.security.oauth2;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.security.AppUserDetails;
import k_webtoons.k_webtoons.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        AppUser user = userDetails.getUser();

        String token = jwtUtil.generateToken(
                user.getUserEmail(),
                user.getRole(),
                user.getIndexId()
        );

        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(false);        // 개발 중에는 false로 설정
        cookie.setSecure(false);          // 로컬 개발용 (배포 시 true)
        cookie.setPath("/");              // 모든 경로에서 쿠키 사용
        cookie.setMaxAge(60 * 60);        // 쿠키 만료 시간: 1시간

        response.addCookie(cookie);

        String redirectUrl = "http://localhost:5173/oauth-redirect";
        response.sendRedirect(redirectUrl);
    }
}