package k_webtoons.k_webtoons.service.admin;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.log.logRepository.UserActivityLogRepository;
import k_webtoons.k_webtoons.model.admin.common.log.KeywordRankResponse;
import k_webtoons.k_webtoons.model.admin.log.PageDwellTimeResponse;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonViewCountResponse;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

   private final UserActivityLogRepository userActivityLogRepository;
   private final WebtoonRepository webtoonRepository;


    // 하루 기준 접속한 사용자 수
    public Long getDailyActiveUsers() {
        return userActivityLogRepository.countDailyActiveUsers();
    }

    // 최근 7일간 방문한 사용자 수
    public Long getRecent7DaysUsers() {
        return userActivityLogRepository.countRecent7DaysUsers();
    }

    // 최근 30일간 방문한 사용자 수
    public Long getRecent30DaysUsers() {
        return userActivityLogRepository.countRecent30DaysUsers();
    }

    // 가장 로그 조회가 많은 웹툰 정보
    public WebtoonViewCountResponse getMostVisitedWebtoonDetail() {
        Long webtoonId = userActivityLogRepository.getMostVisitedWebtoonId();

        if (webtoonId == null) {
            throw new CustomException("가장 많이 조회된 웹툰이 없습니다", "NO_VISITED_WEBTOON");
        }

        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new CustomException("웹툰을 찾을 수 없습니다: " + webtoonId, "WEBTOON_NOT_FOUND"));

        return convertToDto(webtoon);
    }

    private WebtoonViewCountResponse convertToDto(Webtoon webtoon) {
        return new WebtoonViewCountResponse(
                webtoon.getId(),
                webtoon.getTitleId(),
                webtoon.getTitleName(),
                webtoon.getAuthor(),
                webtoon.getAdult(),
                webtoon.getAge(),
                webtoon.getFinish(),
                webtoon.getThumbnailUrl(),
                webtoon.getSynopsis(),
                webtoon.getRankGenreTypes(),
                webtoon.getStarScore(),
                null
        );
    }

    // 검색 키워드 TOP10
    public List<KeywordRankResponse> getTop10Keywords() {
        List<Object[]> results = userActivityLogRepository.getTop10Keywords();
        return results.stream()
                .map(result -> new KeywordRankResponse(
                        (String) result[0],
                        ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }

    // 페이지별 평균 체류 시간
    public List<PageDwellTimeResponse> getPageDwellTimeStats() {
        List<Object[]> results = userActivityLogRepository.getPageDwellTimeStats();
        return results.stream()
                .map(result -> new PageDwellTimeResponse(
                        (String) result[0],
                        ((Number) result[1]).intValue()))
                .collect(Collectors.toList());
    }

}
