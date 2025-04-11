package k_webtoons.k_webtoons.service.webtoon;

import k_webtoons.k_webtoons.exception.WebtoonNotFoundException;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonDetailResponse;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonViewCountResponse;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
public class WebtoonService {

    @Autowired
    private WebtoonRepository webtoonRepository;

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
                webtoon.getStarScore()
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
                webtoon.getStarScore()
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
                webtoon.getStarScore()
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
                webtoon.getStarScore()
        ));
    }

    // 웹툰 ID로 상세 조회
    public WebtoonDetailResponse getWebtoonDetail(Long id) {
        // 1. 기본 정보만 조회 (컬렉션 X)
        Webtoon webtoon = webtoonRepository.findByIdAndIsPublicTrue(id)
                .orElseThrow(() -> new WebtoonNotFoundException("웹툰을 찾을 수 없습니다"));

        // 2. 컬렉션 별도 초기화 (개별 쿼리 실행)
        Hibernate.initialize(webtoon.getGenre()); // SELECT genre WHERE webtoon_id=?
        Hibernate.initialize(webtoon.getTags());  // SELECT tags WHERE webtoon_id=?
        Hibernate.initialize(webtoon.getUserWebtoonReviews());
        Hibernate.initialize(webtoon.getWebtoonComments());

        // 3. DTO 변환
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
                webtoon.getGenre(), // List 유지
                webtoon.getTags(),  // List 유지
                webtoon.getArtistId()
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

}
