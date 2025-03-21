package k_webtoons.k_webtoons.model.webtoon;

import jakarta.persistence.*;
import k_webtoons.k_webtoons.model.favorites.Favorites;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "webtoon")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Webtoon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title_id")
    private Long titleId;

    @Column(name = "title_name", columnDefinition = "TEXT")
    private String titleName;

    @Column(name = "original")
    private Boolean original;

    @Column(name = "author", columnDefinition = "TEXT")
    private String author;

    @Column(name = "artist_id", columnDefinition = "TEXT")
    private String artistId;

    @Column(name = "is_adult")
    private Boolean adult;

    @Column(name = "age", columnDefinition = "TEXT")
    private String age;

    @Column(name = "finish")
    private Boolean finish;

    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(name = "synopsis", columnDefinition = "TEXT")
    private String synopsis;

    @ElementCollection
    @CollectionTable(name = "webtoon_genre", joinColumns = @JoinColumn(name = "webtoon_id"))
    @Column(name = "genre", columnDefinition = "TEXT")
    private List<String> genre;

    @ElementCollection
    @CollectionTable(name = "webtoon_rank_genre_types", joinColumns = @JoinColumn(name = "webtoon_id"))
    @Column(name = "rank_genre_types", columnDefinition = "TEXT")
    private List<String> rankGenreTypes;

    @ElementCollection
    @CollectionTable(name = "webtoon_tags", joinColumns = @JoinColumn(name = "webtoon_id"))
    @Column(name = "tags", columnDefinition = "TEXT")
    private List<String> tags;

    @Column(name = "total_count")
    private Long totalCount;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "star_score")
    private Double starScore;

    @Column(name = "favorite_count")
    private Long favoriteCount;

    @Column(name = "star_std_deviation")
    private Double starStdDeviation;

    @Column(name = "like_mean_value")
    private Double likeMeanValue;

    @Column(name = "like_std_deviation")
    private Double likeStdDeviation;

    @Column(name = "comments_mean_value")
    private Double commentsMeanValue;

    @Column(name = "comments_std_deviation")
    private Double commentsStdDeviation;

    @Column(name = "collected_num_of_epi")
    private Integer collectedNumOfEpi;

    @Column(name = "num_of_works")
    private Integer numOfWorks;

    @Column(name = "nums_of_work_2")
    private Integer numsOfWork2;

    @Column(name = "writers_favor_average")
    private Double writersFavorAverage;

    @Column(name = "osmu_movie")
    private Integer osmuMovie;

    @Column(name = "osmu_drama")
    private Integer osmuDrama;

    @Column(name = "osmu_anime")
    private Integer osmuAnime;

    @Column(name = "osmu_play")
    private Integer osmuPlay;

    @Column(name = "osmu_game")
    private Integer osmuGame;

    @Column(name = "osmu_OX")
    private Integer osmuOX;

    @OneToMany(mappedBy = "webtoon")
    private Set<Favorites> favorites;
}
