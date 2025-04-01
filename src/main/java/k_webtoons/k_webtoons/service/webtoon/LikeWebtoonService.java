package k_webtoons.k_webtoons.service.webtoon;


import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user.LikeWebtoonDTO;
import k_webtoons.k_webtoons.model.webtoon.LikeWebtoonList;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.repository.webtoon.LikeWebtoonListRepository;
import k_webtoons.k_webtoons.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeWebtoonService {

    private final LikeWebtoonListRepository likeWebtoonListRepository;
    private final AuthService authService;
    private final WebtoonService webtoonService;

    public void likeWebtoon(Long webtoonId) {
        try {
            AppUser user = authService.getAuthenticatedUser();
            Webtoon webtoon = webtoonService.getWebtoonById(webtoonId);

            if (likeWebtoonListRepository.existsByAppUserAndWebtoon(user, webtoon)) {
                throw new CustomException("이미 좋아요 한 웹툰입니다.", "ALREADY_LIKED");
            }

            LikeWebtoonList like = new LikeWebtoonList(user, webtoon);
            likeWebtoonListRepository.save(like);
        } catch (Exception e) {
            throw new CustomException("웹툰에 좋아요를 할 수 없습니다.", "LIKE_ERROR");
        }
    }

    public void unlikeWebtoon(Long webtoonId) {
        try {
            AppUser user = authService.getAuthenticatedUser();
            Webtoon webtoon = webtoonService.getWebtoonById(webtoonId);

            LikeWebtoonList like = likeWebtoonListRepository.findByAppUserAndWebtoon(user, webtoon)
                    .orElseThrow(() -> new CustomException("좋아요 기록이 없습니다.", "LIKE_NOT_FOUND"));

            likeWebtoonListRepository.delete(like);
        } catch (Exception e) {
            throw new CustomException("웹툰의 좋아요를 취소할 수 없습니다.", "UNLIKE_ERROR");
        }
    }

    public List<LikeWebtoonDTO> getLikedWebtoonsByUserId(Long userId) {
        try {
            AppUser user = authService.getAuthenticatedUser();
            if (!user.getIndexId().equals(userId)) {
                user = authService.getUserByUserId(userId);
            }

            List<LikeWebtoonList> likedWebtoons = user.getLikeWebtoonLists().stream()
                    .collect(Collectors.toList());

            return likedWebtoons.stream()
                    .map(like -> new LikeWebtoonDTO(
                            like.getWebtoon().getId(),
                            webtoonService.getWebtoonTitleById(like.getWebtoon().getId()),
                            like.getWebtoon().getThumbnailUrl()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("좋아요 한 웹툰을 불러올 수 없습니다.", "WEBTOON_ERROR");
        }
    }
}
