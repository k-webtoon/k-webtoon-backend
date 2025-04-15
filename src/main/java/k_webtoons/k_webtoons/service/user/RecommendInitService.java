package k_webtoons.k_webtoons.service.user;

import jakarta.transaction.Transactional;
import k_webtoons.k_webtoons.repository.user.RecommendInitRepository;
import k_webtoons.k_webtoons.model.user.RecommendInitRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RecommendInitService {

    private final RecommendInitRepository recommendInitRepository;

    @Transactional
    public void saveInitialRecommendations(Long userId, RecommendInitRequestDTO dto) {
        // List<Long> → Long[] 변환
        Long[] webtoonIdsArray = dto.webtoonIds().toArray(new Long[0]);

        recommendInitRepository.insertInitialRecommendations(
                userId,
                webtoonIdsArray // 배열 직접 전달
        );
    }
}