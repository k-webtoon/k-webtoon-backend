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

    @Lob
    private byte[] profileImage;

    private String bio;

    @Column(nullable = false)
    private Boolean isProfilePublic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;



    public UserActivity(byte[] profileImage, String bio, Boolean isProfilePublic, AppUser appUser) {
        this.profileImage = profileImage;
        this.bio = bio;
        this.isProfilePublic = isProfilePublic;
        this.appUser = appUser;
    }
}
