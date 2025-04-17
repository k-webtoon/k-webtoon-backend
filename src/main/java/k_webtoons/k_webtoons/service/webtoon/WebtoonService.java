package k_webtoons.k_webtoons.service.webtoon;

import k_webtoons.k_webtoons.exception.WebtoonNotFoundException;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonDetailResponse;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonViewCountResponse;
import k_webtoons.k_webtoons.repository.webtoon.UserWebtoonReviewRepository;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final UserWebtoonReviewRepository userWebtoonReviewRepository;
    private final ExternalWebtoonApiService externalWebtoonApiService;


    // 조회수 높은 웹툰 리스트 조회 (내림차순 정렬)
    public Page<WebtoonViewCountResponse> getTopWebtoons(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Webtoon> webtoons = webtoonRepository.findTopWebtoons(pageable);

        return webtoons.map(webtoon -> new WebtoonViewCountResponse(
                webtoon.getId(),
                webtoon.getTitleId(),
                webtoon.getTitleName(),
                webtoon.getAuthor(),
                webtoon.getAdult(),
                webtoon.getAge(),
                webtoon.getFinish(),
                webtoon.getThumbnailUrl(),
                webtoon.getSynopsis(),
                new ArrayList<>(webtoon.getRankGenreTypes()),
                webtoon.getStarScore(),
                null
        ));
    }

    // 이름으로 웹툰 검색
    public Page<WebtoonViewCountResponse> searchWebtoonsByName(String titleName, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Webtoon> webtoons = webtoonRepository.findByTitleNameContainingIgnoreCase(titleName, pageable);

        return webtoons.map(webtoon -> new WebtoonViewCountResponse(
                webtoon.getId(),
                webtoon.getTitleId(),
                webtoon.getTitleName(),
                webtoon.getAuthor(),
                webtoon.getAdult(),
                webtoon.getAge(),
                webtoon.getFinish(),
                webtoon.getThumbnailUrl(),
                webtoon.getSynopsis(),
                new ArrayList<>(webtoon.getRankGenreTypes()),
                webtoon.getStarScore(),
                null
        ));
    }

    // 작가로 웹툰 검색
    public Page<WebtoonViewCountResponse> searchWebtoonsByAuthor(String author, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Webtoon> webtoons = webtoonRepository.findByAuthorContaining(author, pageable);

        return webtoons.map(webtoon -> new WebtoonViewCountResponse(
                webtoon.getId(),
                webtoon.getTitleId(),
                webtoon.getTitleName(),
                webtoon.getAuthor(),
                webtoon.getAdult(),
                webtoon.getAge(),
                webtoon.getFinish(),
                webtoon.getThumbnailUrl(),
                webtoon.getSynopsis(),
                new ArrayList<>(webtoon.getRankGenreTypes()),
                webtoon.getStarScore(),
                null
        ));
    }

    // 태그로 웹툰 검색
    public Page<WebtoonViewCountResponse> searchWebtoonsByTags(String tags, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Webtoon> webtoons = webtoonRepository.findByTag(tags, pageable);

        return webtoons.map(webtoon -> new WebtoonViewCountResponse(
                webtoon.getId(),
                webtoon.getTitleId(),
                webtoon.getTitleName(),
                webtoon.getAuthor(),
                webtoon.getAdult(),
                webtoon.getAge(),
                webtoon.getFinish(),
                webtoon.getThumbnailUrl(),
                webtoon.getSynopsis(),
                new ArrayList<>(webtoon.getRankGenreTypes()),
                webtoon.getStarScore(),
                null
        ));
    }

    // 웹툰 ID로 상세 조회
    public WebtoonDetailResponse getWebtoonDetail(Long id) {
        Webtoon webtoon = webtoonRepository.findByIdAndIsPublicTrue(id)
                .orElseThrow(() -> new WebtoonNotFoundException("웹툰을 찾을 수 없습니다"));

        // 별도 쿼리로 컬렉션 데이터 로드
        List<String> genre = webtoonRepository.findGenreByWebtoonId(id);
        List<String> tags = webtoonRepository.findTagsByWebtoonId(id);

        String externalUrl = externalWebtoonApiService.fetchWebtoonUrl(webtoon.getTitleName());

        return new WebtoonDetailResponse(
                webtoon.getId(),
                webtoon.getTitleName(),
                webtoon.getAuthor(),
                webtoon.getThumbnailUrl(),
                webtoon.getSynopsis(),
                webtoon.getAge(),
                String.format("%.2f", webtoon.getStarScore()),
                toBool(webtoon.getOsmuAnime()),
                toBool(webtoon.getOsmuDrama()),
                toBool(webtoon.getOsmuGame()),
                toBool(webtoon.getOsmuMovie()),
                toBool(webtoon.getOsmuOX()),
                toBool(webtoon.getOsmuPlay()),
                webtoon.getFinish(),
                webtoon.getAdult(),
                genre,
                tags,
                webtoon.getArtistId(),
                externalUrl
        );
    }

    // ID로 웹툰 제목 조회
    public String getWebtoonTitleById(Long webtoonId) {
        return webtoonRepository.findTitleById(webtoonId);
    }

    // 웹툰 ID로 웹툰 객체 조회
    public Webtoon getWebtoonById(Long webtoonId) {
        return webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new RuntimeException("웹툰을 찾을 수 없습니다."));
    }

    private boolean toBool(Integer value) {
        return value != null && value == 1;
    }

    // 웹툰을 즐겨찾기 수 순으로 조회
    public List<WebtoonViewCountResponse> getMostFavoritedWebtoons(int size) {
        PageRequest pageable = PageRequest.of(0, size);
        List<Object[]> results = userWebtoonReviewRepository.findMostFavoritedWebtoons(pageable);
        
        List<WebtoonViewCountResponse> popularWebtoons = new ArrayList<>();
        
        for (Object[] result : results) {
            Long webtoonId = (Long) result[0];
            Long totalCount = ((Number) result[1]).longValue();

            Webtoon webtoon = webtoonRepository.findById(webtoonId)
                    .orElseThrow(() -> new WebtoonNotFoundException("웹툰을 찾을 수 없습니다"));

            popularWebtoons.add(new WebtoonViewCountResponse(
                    webtoon.getId(),
                    webtoon.getTitleId(),
                    webtoon.getTitleName(),
                    webtoon.getAuthor(),
                    webtoon.getAdult(),
                    webtoon.getAge(),
                    webtoon.getFinish(),
                    webtoon.getThumbnailUrl(),
                    webtoon.getSynopsis(),
                    new ArrayList<>(webtoon.getRankGenreTypes()),
                    webtoon.getStarScore(),
                    totalCount
            ));
        }

        return popularWebtoons;
    }

    // 웹툰을 좋아요 수 순으로 조회
    public List<WebtoonViewCountResponse> getMostLikedWebtoons(int size) {
        PageRequest pageable = PageRequest.of(0, size);
        List<Object[]> results = userWebtoonReviewRepository.findMostLikedWebtoons(pageable);

        List<WebtoonViewCountResponse> popularWebtoons = new ArrayList<>();

        for (Object[] result : results) {
            Long webtoonId = (Long) result[0];
            Long totalCount = ((Number) result[1]).longValue();

            Webtoon webtoon = webtoonRepository.findById(webtoonId)
                    .orElseThrow(() -> new WebtoonNotFoundException("웹툰을 찾을 수 없습니다"));

            popularWebtoons.add(new WebtoonViewCountResponse(
                    webtoon.getId(),
                    webtoon.getTitleId(),
                    webtoon.getTitleName(),
                    webtoon.getAuthor(),
                    webtoon.getAdult(),
                    webtoon.getAge(),
                    webtoon.getFinish(),
                    webtoon.getThumbnailUrl(),
                    webtoon.getSynopsis(),
                    new ArrayList<>(webtoon.getRankGenreTypes()),
                    webtoon.getStarScore(),
                    totalCount
            ));
        }

        return popularWebtoons;
    }

    // 웹툰을 봤어요 수 순으로 조회
    public List<WebtoonViewCountResponse> getMostWatchedWebtoons(int size) {
        PageRequest pageable = PageRequest.of(0, size);
        List<Object[]> results = userWebtoonReviewRepository.findMostWatchedWebtoons(pageable);

        List<WebtoonViewCountResponse> popularWebtoons = new ArrayList<>();

        for (Object[] result : results) {
            Long webtoonId = (Long) result[0];
            Long totalCount = ((Number) result[1]).longValue();

            Webtoon webtoon = webtoonRepository.findById(webtoonId)
                    .orElseThrow(() -> new WebtoonNotFoundException("웹툰을 찾을 수 없습니다"));

            popularWebtoons.add(new WebtoonViewCountResponse(
                    webtoon.getId(),
                    webtoon.getTitleId(),
                    webtoon.getTitleName(),
                    webtoon.getAuthor(),
                    webtoon.getAdult(),
                    webtoon.getAge(),
                    webtoon.getFinish(),
                    webtoon.getThumbnailUrl(),
                    webtoon.getSynopsis(),
                    new ArrayList<>(webtoon.getRankGenreTypes()),
                    webtoon.getStarScore(),
                    totalCount
            ));
        }
        
        return popularWebtoons;
    }
}
