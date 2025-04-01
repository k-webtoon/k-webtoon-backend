package k_webtoons.k_webtoons.security;

import k_webtoons.k_webtoons.model.auth.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// 이곳에서 시큐리티에게 권한과 사용자 엔티티를 알려주고 있음 여기서 추가적인 권한 관련 로직 추가
public class AppUserDetails implements UserDetails {
    private final AppUser user;

    public AppUserDetails(AppUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserEmail();
    }

    public String getRole() {
        return user.getRole();
    }
}
