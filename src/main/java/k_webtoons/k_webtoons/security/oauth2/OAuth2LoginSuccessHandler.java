package k_webtoons.k_webtoons.security.oauth2;


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

        String redirectUrl = "http://localhost:5173/oauth-redirect?token=" + token;
        response.sendRedirect(redirectUrl); // 바로 리다이렉트
    }
}