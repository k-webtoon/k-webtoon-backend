package k_webtoons.k_webtoons.security;

import k_webtoons.k_webtoons.model.auth.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * AppUserDetails는 Spring Security와 OAuth2 로그인을 처리하기 위한 사용자 정보 클래스입니다.
 * - UserDetails: 일반 로그인 처리
 * - OAuth2User: OAuth2 로그인 처리
 */
public class AppUserDetails implements UserDetails, OAuth2User {
    private final AppUser user; // 사용자 엔티티
    private final Map<String, Object> attributes; // OAuth2 사용자 정보 (Google 등에서 제공)

    /**
     * 일반 로그인용 생성자
     *
     * @param user 사용자 엔티티
     */
    public AppUserDetails(AppUser user) {
        this.user = user;
        this.attributes = Collections.emptyMap(); // 일반 로그인에서는 속성 없음
    }

    /**
     * OAuth2 로그인용 생성자
     *
     * @param user 사용자 엔티티
     * @param attributes OAuth2 사용자 정보
     */
    public AppUserDetails(AppUser user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes; // Google 등에서 제공하는 속성 저장
    }

    /**
     * 사용자 엔티티 반환
     *
     * @return AppUser 엔티티
     */
    public AppUser getUser() {
        return this.user;
    }

    /**
     * 권한 반환 (Spring Security)
     *
     * @return Collection of GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    /**
     * 사용자 비밀번호 반환 (Spring Security)
     *
     * @return 비밀번호 (암호화된 상태)
     */
    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    /**
     * 사용자 이메일 반환 (Spring Security)
     *
     * @return 이메일 (username 역할)
     */
    @Override
    public String getUsername() {
        return user.getUserEmail();
    }

    /**
     * 사용자 역할 반환
     *
     * @return 역할 (예: USER, ADMIN)
     */
    public String getRole() {
        return user.getRole();
    }

    /**
     * 계정이 잠겨 있는지 확인 (Spring Security)
     *
     * @return true if account is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return user.getAccountStatus() != AccountStatus.SUSPENDED; // 정지 상태인지 확인
    }

    /**
     * 계정이 활성화되어 있는지 확인 (Spring Security)
     *
     * @return true if account is enabled
     */
    @Override
    public boolean isEnabled() {
        return user.getAccountStatus() == AccountStatus.ACTIVE; // 활성 상태인지 확인
    }

    /**
     * 계정이 만료되지 않았는지 확인 (Spring Security)
     *
     * @return true if account is not expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // 만료 관련 로직이 필요하면 추가 가능
    }

    /**
     * 자격 증명이 만료되지 않았는지 확인 (Spring Security)
     *
     * @return true if credentials are not expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 관련 로직이 필요하면 추가 가능
    }

    /**
     * OAuth2 속성 반환 (OAuth2User)
     *
     * @return Map of attributes provided by OAuth2 provider (Google 등)
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes; // Google 등에서 제공하는 속성 반환
    }

    /**
     * OAuth2 이름 반환 (OAuth2User)
     *
     * @return 사용자 이름 또는 고유 식별자 (Google의 경우 sub 사용 가능)
     */
    @Override
    public String getName() {
        return user.getNickname(); // 닉네임을 이름으로 사용 (필요 시 수정 가능)
    }
}
