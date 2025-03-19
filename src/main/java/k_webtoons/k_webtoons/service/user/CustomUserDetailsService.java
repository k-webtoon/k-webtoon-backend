package k_webtoons.k_webtoons.service.user;

import k_webtoons.k_webtoons.model.user.AppUser;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    @Lazy
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        AppUser user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        return new AppUserDetails(user);
    }
}

