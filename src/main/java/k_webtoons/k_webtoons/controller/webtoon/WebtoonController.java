package k_webtoons.k_webtoons.controller.webtoon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import k_webtoons.k_webtoons.model.webtoon.WebtoonViewCountResponse;
import k_webtoons.k_webtoons.service.webtoon.WebtoonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebtoonController {

    @Autowired
    private WebtoonService webtoonService;

    // 조회수 높은 웹툰 리스트 조회 API
    @Operation(
            summary = "조회수 높은 웹툰 리스트 조회",
            description = "조회수가 높은 웹툰 목록을 페이지네이션 형태로 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 웹툰 목록 반환")
    })
    @GetMapping("/webtoons/top")
    public Page<WebtoonViewCountResponse> getTopWebtoons(
            @Parameter(description = "페이지 번호 (기본값: 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기 (기본값: 10)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return webtoonService.getTopWebtoons(page, size);
    }

    // 이름으로 웹툰 검색 API
    @Operation(
            summary = "웹툰 이름으로 검색",
            description = "제목 이름을 기반으로 웹툰을 검색하여 페이지네이션 형태로 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 웹툰 검색 결과 반환")
    })
    @GetMapping("/webtoons/search/name")
    public Page<WebtoonViewCountResponse> searchWebtoonsByName(
            @Parameter(description = "검색할 웹툰 제목", required = true, example = "웹툰 제목(ex: 마음의 소리)")
            @RequestParam String titleName,
            @Parameter(description = "페이지 번호 (기본값: 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기 (기본값: 10)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return webtoonService.searchWebtoonsByName(titleName, page, size);
    }

    // 작가로 웹툰 검색 API
    @Operation(
            summary = "웹툰 작가로 검색",
            description = "작가 기반으로 웹툰을 검색하여 페이지네이션 형태로 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 웹툰 검색 결과 반환")
    })
    @GetMapping("/webtoons/search/author")
    public Page<WebtoonViewCountResponse> searchWebtoonsByAuthor(
            @Parameter(description = "검색할 작가", required = true, example = "작가(ex: 조석)")
            @RequestParam String authorName,
            @Parameter(description = "페이지 번호 (기본값: 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기 (기본값: 10)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return webtoonService.searchWebtoonsByAuthor(authorName, page, size);
    }

    // 테그로 웹툰 검색 API
    @Operation(
            summary = "웹툰 테그로 검색",
            description = "테그 기반으로 웹툰을 검색하여 페이지네이션 형태로 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 웹툰 검색 결과 반환")
    })
    @GetMapping("/webtoons/search/tag")
    public Page<WebtoonViewCountResponse> searchWebtoonsByTags(
            @Parameter(description = "검색할 테그", required = true, example = "테그(ex: 하이틴)")
            @RequestParam String tagName,
            @Parameter(description = "페이지 번호 (기본값: 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기 (기본값: 10)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return webtoonService.searchWebtoonsByTags(tagName, page, size);
    }

}