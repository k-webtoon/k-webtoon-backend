package k_webtoons.k_webtoons.model.likeWebtoonList;

import jakarta.persistence.*;
import k_webtoons.k_webtoons.model.user.AppUser;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;

@Entity
@Table(name = "like_webtoon_list")
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

}
