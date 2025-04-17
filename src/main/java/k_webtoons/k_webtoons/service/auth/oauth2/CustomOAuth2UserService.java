package k_webtoons.k_webtoons.service.auth.oauth2;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.security.AppUserDetails;
import k_webtoons.k_webtoons.service.user.UserActivityService;
import lombok.RequiredArgsConstructor;
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

        final String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = null;
        String name = null;

        if ("google".equalsIgnoreCase(registrationId)) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        } else if ("kakao".equalsIgnoreCase(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount != null) {
                // 이메일 추출 (account_email 동의 필요)
                email = (String) kakaoAccount.get("email");
                // 닉네임 추출 (profile_nickname 동의 필요)
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile != null) {
                    name = (String) profile.get("nickname");
                }
            }
        }

        // 닉네임 폴백 처리 (이메일 아이디 or "kakao_user")
        if (name == null || name.isBlank()) {
            name = (email != null && email.contains("@"))
                    ? email.split("@")[0]
                    : "kakao_user";
        }

        // 이메일이 반드시 있어야 함
        if (email == null) {
            throw new OAuth2AuthenticationException("카카오 이메일 정보를 가져오지 못했습니다.");
        }

        final String provider = registrationId.toUpperCase();
        final String finalEmail = email;
        final String finalName = name;

        AppUser user = userRepository.findByUserEmailAndProvider(finalEmail, provider)
                .orElseGet(() -> createOAuth2User(finalEmail, finalName, provider));

        return new AppUserDetails(user, attributes);
    }

    private AppUser createOAuth2User(String email, String name, String provider) {
        AppUser newUser = new AppUser(
                email,
                passwordEncoder.encode("OAUTH2_DUMMY_PASSWORD"),
                null, // userAge
                null, // gender
                name,
                "USER",
                null, // phoneNumber
                null, // securityQuestion
                null, // securityAnswer
                LocalDateTime.now(),
                provider
        );

        AppUser savedUser = userRepository.save(newUser);
        userActivityService.createEmptyUserActivity(savedUser);
        return savedUser;
    }
}