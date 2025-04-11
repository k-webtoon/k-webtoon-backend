package k_webtoons.k_webtoons.service.user;

import jakarta.transaction.Transactional;
import k_webtoons.k_webtoons.repository.user.RecommendInitRepository;
import k_webtoons.k_webtoons.model.user.RecommendInitRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional // ✅ 여기가 핵심!
public class RecommendInitService {

    private final RecommendInitRepository repository;

    public void saveInitialRecommendations(Long userId, RecommendInitRequestDTO dto) {
        repository.insertInitialRecommendations(userId, dto.getWebtoonIds());
    }
}