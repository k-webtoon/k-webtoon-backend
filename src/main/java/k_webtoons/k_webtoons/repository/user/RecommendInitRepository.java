package k_webtoons.k_webtoons.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecommendInitRepository {

    @PersistenceContext
    private EntityManager em;

    public void insertInitialRecommendations(Long userId, List<Long> webtoonIds) {
        for (Long webtoonId : webtoonIds) {
            em.createNativeQuery("""
                INSERT INTO public.user_webtoon_review 
                (user_index_id, webtoon_id, is_favorite, is_liked, is_watched, rating)
                VALUES (:userId, :webtoonId, true, true, false, null)
            """)
                    .setParameter("userId", userId)
                    .setParameter("webtoonId", webtoonId)
                    .executeUpdate();
        }
    }
}