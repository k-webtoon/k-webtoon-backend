package k_webtoons.k_webtoons.controller.webtoon;


import k_webtoons.k_webtoons.model.user.LikeWebtoonDTO;
import k_webtoons.k_webtoons.service.webtoon.LikeWebtoonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/webtoon/like")
public class LikeWebtoonController {

    @Autowired
    private LikeWebtoonService likeWebtoonService;

    @PostMapping("/{webtoonId}")
    public ResponseEntity<Void> likeWebtoon(@PathVariable Long webtoonId) {
        likeWebtoonService.likeWebtoon(webtoonId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{webtoonId}/unlike")
    public ResponseEntity<Void> unlikeWebtoon(@PathVariable Long webtoonId) {
        likeWebtoonService.unlikeWebtoon(webtoonId);
        return ResponseEntity.ok().build();
    }


}

