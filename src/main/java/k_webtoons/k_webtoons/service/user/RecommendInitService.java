package k_webtoons.k_webtoons.service.user;

import jakarta.transaction.Transactional;
import k_webtoons.k_webtoons.repository.user.RecommendInitRepository;
import k_webtoons.k_webtoons.model.user.RecommendInitRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendInitService {

    private final RecommendInitRepository recommendInitRepository;

    @Transactional
    public void saveInitialRecommendations(Long userId, RecommendInitRequestDTO dto) {
        recommendInitRepository.insertInitialRecommendations(userId, dto.webtoonIds());
    }
}