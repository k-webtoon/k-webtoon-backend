package k_webtoons.k_webtoons.repository.user;

import k_webtoons.k_webtoons.model.webtoon.UserWebtoonReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecommendInitRepository extends JpaRepository<UserWebtoonReview, Long> {

    @Modifying
    @Query(value = """
            INSERT INTO user_webtoon_review
            (user_index_id, webtoon_id, is_favorite, is_liked, is_watched, rating)
            SELECT
                CAST(:userId AS BIGINT),
                CAST(webtoon_id AS BIGINT),
                true, true, false, null
            FROM unnest(CAST(:webtoonIds AS BIGINT[])) AS webtoon_id
            """, nativeQuery = true)
    void insertInitialRecommendations(
            @Param("userId") Long userId,
            @Param("webtoonIds") Long[] webtoonIds
    );
}