package k_webtoons.k_webtoons.service.webtoon;

import k_webtoons.k_webtoons.exception.WebtoonNotFoundException;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoon.WebtoonDetailResponse;
import k_webtoons.k_webtoons.model.webtoon.WebtoonViewCountResponse;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WebtoonService {

    @Autowired
    private WebtoonRepository webtoonRepository;

    // 조회수 높은 웹툰 리스트 조회
    public Page<WebtoonViewCountResponse> getTopWebtoons(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Webtoon> webtoons = webtoonRepository.findAllByOrderByViewCountDesc(pageable);

        return webtoons.map(webtoon ->
                new WebtoonViewCountResponse(
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
                        webtoon.getStarScore()
                ));
    }

    // 이름으로 웹툰 검색
    public Page<WebtoonViewCountResponse> searchWebtoonsByName(String titleName, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Webtoon> webtoons = webtoonRepository.findByTitleNameContainingIgnoreCase(titleName, pageable);

        return webtoons.map(webtoon ->
                new WebtoonViewCountResponse(
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
                        webtoon.getStarScore()
                ));
    }

    // 작가로 웹툰 검색
    public Page<WebtoonViewCountResponse> searchWebtoonsByAuthor(String author, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Webtoon> webtoons = webtoonRepository.findByAuthorContaining(author, pageable);

        return webtoons.map(webtoon ->
                new WebtoonViewCountResponse(
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
                        webtoon.getStarScore()
                ));
    }

    // 테그로 웹툰 검색
    public Page<WebtoonViewCountResponse> searchWebtoonsByTags(String tags, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Webtoon> webtoons = webtoonRepository.findByTag(tags, pageable);

        return webtoons.map(webtoon ->
                new WebtoonViewCountResponse(
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
                        webtoon.getStarScore()
                ));
    }

    public WebtoonDetailResponse getWebtoonDetail(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new WebtoonNotFoundException("해당 ID의 웹툰이 존재하지 않습니다."));

        return new WebtoonDetailResponse(
                webtoon.getId(),
                webtoon.getTitleName(),
                webtoon.getAuthor(),
                webtoon.getThumbnailUrl(),
                webtoon.getSynopsis(),
                webtoon.getAge(),
                String.format("%.2f", webtoon.getStarScore() != null ? webtoon.getStarScore() : 0.0),
                toBool(webtoon.getOsmuAnime()),
                toBool(webtoon.getOsmuDrama()),
                toBool(webtoon.getOsmuGame()),
                toBool(webtoon.getOsmuMovie()),
                toBool(webtoon.getOsmuOX()),
                toBool(webtoon.getOsmuPlay()),
                webtoon.getFinish(),
                webtoon.getAdult(),
                webtoon.getGenre(),
                webtoon.getTags(),
                webtoon.getArtistId()
        );

    }


    // 여기서부턴 로직용 함수

    public String getWebtoonTitleById(Long webtoonId) {
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new WebtoonNotFoundException("해당 ID의 웹툰이 존재하지 않습니다."));
        return webtoon.getTitleName();
    }

    public Webtoon getWebtoonById(Long webtoonId) {
        return webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new RuntimeException("웹툰을 찾을 수 없습니다."));
    }

    private boolean toBool(Integer value) {
        return value != null && value == 1;
    }


}


