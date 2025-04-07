package k_webtoons.k_webtoons.service.webtoon;

import k_webtoons.k_webtoons.exception.WebtoonNotFoundException;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoon.WebtoonDetailResponse;
import k_webtoons.k_webtoons.model.webtoon.WebtoonViewCountResponse;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebtoonServiceTest {

    @Mock
    private WebtoonRepository webtoonRepository;

    @InjectMocks
    private WebtoonService webtoonService;

    @Test
    @DisplayName("인기 웹툰 목록 조회 테스트")
    void getTopWebtoonsTest() {
        // Given
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        
        List<Webtoon> webtoonList = new ArrayList<>();
        Webtoon webtoon1 = createMockWebtoon(1L, "인기웹툰1", "작가1", false, "전체이용가", 4.5);
        Webtoon webtoon2 = createMockWebtoon(2L, "인기웹툰2", "작가2", false, "15세", 4.3);
        webtoonList.add(webtoon1);
        webtoonList.add(webtoon2);
        
        Page<Webtoon> webtoonPage = new PageImpl<>(webtoonList);
        
        when(webtoonRepository.findAllByOrderByFavoriteCountDesc(pageRequest)).thenReturn(webtoonPage);

        // When
        Page<WebtoonViewCountResponse> result = webtoonService.getTopWebtoons(page, size);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).titleName()).isEqualTo("인기웹툰1");
        assertThat(result.getContent().get(1).titleName()).isEqualTo("인기웹툰2");
        
        verify(webtoonRepository, times(1)).findAllByOrderByFavoriteCountDesc(pageRequest);
    }

    @Test
    @DisplayName("이름으로 웹툰 검색 테스트")
    void searchWebtoonsByNameTest() {
        // Given
        String titleName = "인기";
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        
        List<Webtoon> webtoonList = new ArrayList<>();
        Webtoon webtoon = createMockWebtoon(1L, "인기웹툰1", "작가1", false, "전체이용가", 4.5);
        webtoonList.add(webtoon);
        
        Page<Webtoon> webtoonPage = new PageImpl<>(webtoonList);
        
        when(webtoonRepository.findByTitleNameContainingIgnoreCase(titleName, pageRequest)).thenReturn(webtoonPage);

        // When
        Page<WebtoonViewCountResponse> result = webtoonService.searchWebtoonsByName(titleName, page, size);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).titleName()).isEqualTo("인기웹툰1");
        assertThat(result.getContent().get(0).author()).isEqualTo("작가1");
        
        verify(webtoonRepository, times(1)).findByTitleNameContainingIgnoreCase(titleName, pageRequest);
    }

    @Test
    @DisplayName("작가로 웹툰 검색 테스트")
    void searchWebtoonsByAuthorTest() {
        // Given
        String author = "작가";
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        
        List<Webtoon> webtoonList = new ArrayList<>();
        Webtoon webtoon = createMockWebtoon(1L, "인기웹툰1", "작가1", false, "전체이용가", 4.5);
        webtoonList.add(webtoon);
        
        Page<Webtoon> webtoonPage = new PageImpl<>(webtoonList);
        
        when(webtoonRepository.findByAuthorContaining(author, pageRequest)).thenReturn(webtoonPage);

        // When
        Page<WebtoonViewCountResponse> result = webtoonService.searchWebtoonsByAuthor(author, page, size);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).titleName()).isEqualTo("인기웹툰1");
        assertThat(result.getContent().get(0).author()).isEqualTo("작가1");
        
        verify(webtoonRepository, times(1)).findByAuthorContaining(author, pageRequest);
    }

    @Test
    @DisplayName("태그로 웹툰 검색 테스트")
    void searchWebtoonsByTagsTest() {
        // Given
        String tag = "코믹";
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        
        List<Webtoon> webtoonList = new ArrayList<>();
        Webtoon webtoon = createMockWebtoon(1L, "인기웹툰1", "작가1", false, "전체이용가", 4.5);
        webtoonList.add(webtoon);
        
        Page<Webtoon> webtoonPage = new PageImpl<>(webtoonList);
        
        when(webtoonRepository.findByTag(tag, pageRequest)).thenReturn(webtoonPage);

        // When
        Page<WebtoonViewCountResponse> result = webtoonService.searchWebtoonsByTags(tag, page, size);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).titleName()).isEqualTo("인기웹툰1");
        
        verify(webtoonRepository, times(1)).findByTag(tag, pageRequest);
    }

    @Test
    @DisplayName("웹툰 상세 조회 테스트")
    void getWebtoonDetailTest() {
        // Given
        Long webtoonId = 1L;
        Webtoon webtoon = createMockWebtoon(webtoonId, "인기웹툰1", "작가1", false, "전체이용가", 4.5);
        
        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.of(webtoon));

        // When
        WebtoonDetailResponse result = webtoonService.getWebtoonDetail(webtoonId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(webtoonId);
        assertThat(result.titleName()).isEqualTo("인기웹툰1");
        assertThat(result.author()).isEqualTo("작가1");
        
        verify(webtoonRepository, times(1)).findById(webtoonId);
    }

    @Test
    @DisplayName("존재하지 않는 웹툰 ID로 조회 시 예외 발생")
    void getWebtoonDetailNotFoundTest() {
        // Given
        Long nonExistingId = 999L;
        when(webtoonRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(WebtoonNotFoundException.class, () -> webtoonService.getWebtoonDetail(nonExistingId));
        
        verify(webtoonRepository, times(1)).findById(nonExistingId);
    }

    @Test
    @DisplayName("웹툰 제목 조회 테스트")
    void getWebtoonTitleByIdTest() {
        // Given
        Long webtoonId = 1L;
        Webtoon webtoon = createMockWebtoon(webtoonId, "인기웹툰1", "작가1", false, "전체이용가", 4.5);
        
        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.of(webtoon));

        // When
        String title = webtoonService.getWebtoonTitleById(webtoonId);

        // Then
        assertThat(title).isEqualTo("인기웹툰1");
        
        verify(webtoonRepository, times(1)).findById(webtoonId);
    }

    @Test
    @DisplayName("웹툰 객체 조회 테스트")
    void getWebtoonByIdTest() {
        // Given
        Long webtoonId = 1L;
        Webtoon webtoon = createMockWebtoon(webtoonId, "인기웹툰1", "작가1", false, "전체이용가", 4.5);
        
        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.of(webtoon));

        // When
        Webtoon result = webtoonService.getWebtoonById(webtoonId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(webtoonId);
        assertThat(result.getTitleName()).isEqualTo("인기웹툰1");
        
        verify(webtoonRepository, times(1)).findById(webtoonId);
    }

    private Webtoon createMockWebtoon(Long id, String titleName, String author, Boolean adult, String age, Double starScore) {
        Webtoon webtoon = new Webtoon();
        webtoon.setId(id);
        webtoon.setTitleId(id);
        webtoon.setTitleName(titleName);
        webtoon.setAuthor(author);
        webtoon.setAdult(adult);
        webtoon.setAge(age);
        webtoon.setFinish(false);
        webtoon.setThumbnailUrl("thumbnail.jpg");
        webtoon.setSynopsis("웹툰 줄거리입니다.");
        
        List<String> genres = new ArrayList<>();
        genres.add("코믹");
        genres.add("로맨스");
        webtoon.setGenre(genres);
        
        List<String> tags = new ArrayList<>();
        tags.add("학원물");
        tags.add("성장");
        webtoon.setTags(tags);
        
        webtoon.setRankGenreTypes(List.of("코믹"));
        webtoon.setStarScore(starScore);
        webtoon.setArtistId("artist123");
        
        return webtoon;
    }
} 