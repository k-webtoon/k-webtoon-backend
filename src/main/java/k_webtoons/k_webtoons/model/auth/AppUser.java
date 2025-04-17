package k_webtoons.k_webtoons.model.auth;

import jakarta.persistence.*;
import k_webtoons.k_webtoons.model.user.userActivity.UserActivity;
import k_webtoons.k_webtoons.model.webtoon.RecommendWebtoon;
import k_webtoons.k_webtoons.model.webtoon.UserWebtoonReview;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import k_webtoons.k_webtoons.security.AccountStatus;
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
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "index_id")
    private Long indexId;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @JsonIgnore  // 민감 정보 숨기기
    @Column(nullable = false)
    private String userPassword;

    private LocalDateTime createDateTime;

    private LocalDateTime deletedDateTime;

    @Column(name = "suspended_at")
    private LocalDateTime suspendedAt; // 정지 시작 시간
    @Column(name = "user_age")
    private Integer userAge;

    private String gender;

    private String nickname;

    @Column(nullable = false)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @OneToMany(mappedBy = "appUser")
    private Set<UserWebtoonReview> userWebtoonReviews;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    private List<WebtoonComment> webtoonComments;  // 사용자가 작성한 웹툰 댓글 목록

    @OneToMany(mappedBy = "appUser")
    private Set<RecommendWebtoon> recommendWebtoons;

    @Column(unique = true)
    private String phoneNumber;

    private String securityQuestion;

    @JsonIgnore
    private String securityAnswer;

    private String provider;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserActivity userActivity;

    public AppUser(String userEmail, String userPassword, Integer userAge, String gender, String nickname, String role, String phoneNumber, String securityQuestion, String securityAnswer, LocalDateTime createDateTime, String provider) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userAge = userAge;
        this.gender = gender;
        this.nickname = nickname;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.createDateTime = createDateTime;
        this.accountStatus = AccountStatus.ACTIVE;
        this.provider = provider;
    }

    // Spring Security 권한 처리
    public List<SimpleGrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
}
