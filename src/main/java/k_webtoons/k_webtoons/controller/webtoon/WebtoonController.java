package k_webtoons.k_webtoons.controller.webtoon;


import k_webtoons.k_webtoons.model.webtoon.WebtoonViewCountResponse;
import k_webtoons.k_webtoons.service.webtoon.WebtoonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebtoonController {

    @Autowired
    private WebtoonService webtoonService;

    // 조회수 높은 웹툰 리스트 조회 API
    @GetMapping("/webtoons/top")
    public Page<WebtoonViewCountResponse> getTopWebtoons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return webtoonService.getTopWebtoons(page, size);
    }

    // 이름으로 웹툰 검색 API
    @GetMapping("/webtoons/search")
    public Page<WebtoonViewCountResponse> searchWebtoonsByName(
            @RequestParam String titleName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return webtoonService.searchWebtoonsByName(titleName, page, size);
    }
}