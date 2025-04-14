package k_webtoons.k_webtoons.model.webtoon;

import jakarta.persistence.*;
import k_webtoons.k_webtoons.model.auth.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@Table(name = "user_webtoon_review")
@AllArgsConstructor
@NoArgsConstructor
public class UserWebtoonReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = true)
    private Integer rating; // 평점

    @Column
    private Boolean isLiked; // 좋아요

    @Column
    private Boolean isFavorite; // 즐겨찾기 여부

    @Column
    private Boolean isWatched; // 봤어요 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_index_id", nullable = false)
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    // 생성자 (좋아요 전용)
    public UserWebtoonReview(AppUser appUser, Webtoon webtoon, Boolean isLiked) {
        this.appUser = appUser;
        this.webtoon = webtoon;
        this.isLiked = isLiked;
    }

    // 생성자 (평점 전용)
    public UserWebtoonReview(AppUser appUser, Webtoon webtoon, Integer rating) {
        this.appUser = appUser;
        this.webtoon = webtoon;
        this.rating = rating;
    }

    public UserWebtoonReview(Integer rating, Boolean isLiked, Boolean isFavorite, Boolean isWatched, AppUser appUser, Webtoon webtoon) {
        this.rating = rating;
        this.isLiked = isLiked;
        this.isFavorite = isFavorite;
        this.isWatched = isWatched;
        this.appUser = appUser;
        this.webtoon = webtoon;
    }
}
