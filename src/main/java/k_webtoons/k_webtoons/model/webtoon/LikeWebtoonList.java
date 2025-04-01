package k_webtoons.k_webtoons.model.webtoon;

import jakarta.persistence.*;
import k_webtoons.k_webtoons.model.auth.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "like_webtoon_list")
@AllArgsConstructor
@NoArgsConstructor
public class LikeWebtoonList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_index_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;

    public LikeWebtoonList(AppUser appUser, Webtoon webtoon) {
        this.appUser = appUser;
        this.webtoon = webtoon;
    }
}
