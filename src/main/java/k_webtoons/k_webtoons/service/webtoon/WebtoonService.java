package k_webtoons.k_webtoons.service.webtoon;

import k_webtoons.k_webtoons.model.webtoon.Webtoon;
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

}
