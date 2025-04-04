package k_webtoons.k_webtoons.model.auth;

import jakarta.persistence.*;
import k_webtoons.k_webtoons.model.webtoon.LikeWebtoonList;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "index_id")
    private Long indexId;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @JsonIgnore  // ✅ 민감 정보 숨기기
    @Column(nullable = false)
    private String userPassword;

    private LocalDateTime createDateTime;

    private LocalDateTime deletedDateTime;

    private Integer userAge;

    private String gender;

    private String nickname;

    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy = "appUser")
    private Set<LikeWebtoonList> likeWebtoonLists ;

    @OneToMany(mappedBy = "appUser")
    private List<WebtoonComment> webtoonComments;  // 사용자가 작성한 웹툰 댓글 목록

    @Column(unique = true)
    private String phoneNumber;

    private String securityQuestion;

    @JsonIgnore
    private String securityAnswer;

    public AppUser(String userEmail, String userPassword, Integer userAge, String gender, String nickname, String role, String phoneNumber, String securityQuestion, String securityAnswer) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userAge = userAge;
        this.gender = gender;
        this.nickname = nickname;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    // Spring Security 권한 처리
    public List<SimpleGrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
}
