package k_webtoons.k_webtoons.service.auth.oauth2;

import k_webtoons.k_webtoons.config.SecurityConfig;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.security.AppUserDetails;
import k_webtoons.k_webtoons.service.user.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserActivityService userActivityService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Google에서 제공하는 정보 추출
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase(); // 공급자 정보 추출

        // 기존 사용자 조회 또는 새 사용자 생성
        AppUser user = userRepository.findByUserEmail(email)
                .orElseGet(() -> createOAuth2User(email, name, provider)); // provider 파라미터 추가

        return new AppUserDetails(user, attributes);
    }

    private AppUser createOAuth2User(String email, String name, String provider) {
        // 필수 필드만 설정 (나머지는 null 허용)
        AppUser newUser = new AppUser(
                email,
                passwordEncoder.encode("OAUTH2_DUMMY_PASSWORD"),
                null,
                null,
                name,
                "USER",
                null,
                null,
                null,
                LocalDateTime.now(),
                provider
        );

        AppUser savedUser = userRepository.save(newUser);
        userActivityService.createEmptyUserActivity(savedUser); // UserActivity 생성 추가
        return savedUser;
    }
}