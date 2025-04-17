package k_webtoons.k_webtoons.service.admin;

import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebtoonStatsService {
    private final WebtoonRepository webtoonRepository;
    // TODO: 웹툰 수 / 장르 / 평점 등 쿼리 메서드 작성
}

