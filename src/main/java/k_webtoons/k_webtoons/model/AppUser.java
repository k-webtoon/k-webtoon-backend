package k_webtoons.k_webtoons.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // PostgreSQL에서 자동 증가 필드로 처리
    @Column(name = "index_id")
    private Long indexId;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String userPassword;

    private LocalDateTime createDateTime;
    private LocalDateTime deletedDateTime;
    private Integer userAge;
    private String gender;
    private String nickname;

    // 기본 생성자 외에 필요한 필드를 가지는 생성자 추가
    public AppUser(String userEmail, String encodedPassword, Integer userAge, String gender, String nickname) {
        this.userEmail = userEmail;
        this.userPassword = encodedPassword;
        this.userAge = userAge;
        this.gender = gender;
        this.nickname = nickname;
        this.createDateTime = LocalDateTime.now();
    }

    // Spring Security 권한 처리
    public List<SimpleGrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
