package k_webtoons.k_webtoons.security;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HeaderValidator {

    private final UserRepository userRepository;

    /**
     * 인증된 사용자 정보를 반환합니다.
     *
     * @return AppUser 인증된 사용자 객체
     * @throws RuntimeException 인증되지 않은 사용자이거나, 사용자 정보를 찾을 수 없는 경우 예외 발생
     */

    public AppUser getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails)) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }

        String userEmail = ((UserDetails) principal).getUsername();
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}