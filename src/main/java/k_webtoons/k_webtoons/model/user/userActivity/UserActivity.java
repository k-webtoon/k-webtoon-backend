package k_webtoons.k_webtoons.model.user.userActivity;

import jakarta.persistence.*;
import k_webtoons.k_webtoons.model.auth.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String profileImageUrl;

    private String bio;

    @Column(nullable = false)
    private Boolean isProfilePublic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;

    public UserActivity(String profileImageUrl, String bio, Boolean isProfilePublic, AppUser appUser) {
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
        this.isProfilePublic = isProfilePublic;
        this.appUser = appUser;
    }
}
