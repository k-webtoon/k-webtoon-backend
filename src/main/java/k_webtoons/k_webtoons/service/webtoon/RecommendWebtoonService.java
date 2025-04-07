package k_webtoons.k_webtoons.service.webtoon;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.RecommendWebtoon;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.repository.webtoon.RecommendWebtoonRepository;
import k_webtoons.k_webtoons.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecommendWebtoonService {

    private final RecommendWebtoonRepository recommendRepository;
    private final AuthService authService;
    private final WebtoonService webtoonService;

    // 추천 토글
    @Transactional
    public void toggleRecommend(Long webtoonId) {
        AppUser user = authService.getAuthenticatedUser();
        Webtoon webtoon = webtoonService.getWebtoonById(webtoonId);

        RecommendWebtoon recommend = recommendRepository.findByAppUserAndWebtoon(user, webtoon)
                .orElseGet(() -> new RecommendWebtoon(false, LocalDateTime.now() ,user, webtoon));

        recommendRepository.save(recommend);
    }
}