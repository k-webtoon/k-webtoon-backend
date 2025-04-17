package k_webtoons.k_webtoons.service.admin;

import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebtoonStatsService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonCommentRepository webtoonCommentRepository;

    // 전체 웹툰 수
    public Long getTotalWebtoonCount() {
        return webtoonRepository.count();
    }

    // 장르별 웹툰 수
    public Map<String, Long> getGenreDistribution() {
        List<Webtoon> webtoons = webtoonRepository.findAllWithGenre(); // 수정된 부분
        Map<String, Long> genreCount = new HashMap<>();
        for (Webtoon webtoon : webtoons) {
            if (webtoon.getGenre() != null) {
                for (String genre : webtoon.getGenre()) {
                    genreCount.put(genre, genreCount.getOrDefault(genre, 0L) + 1);
                }
            }
        }
        return genreCount;
    }


    // OSMU 비율
    public Map<String, Long> getOsmuRatio() {
        List<Webtoon> webtoons = webtoonRepository.findAll();
        long movie = webtoons.stream().filter(w -> w.getOsmuMovie() != null && w.getOsmuMovie() > 0).count();
        long drama = webtoons.stream().filter(w -> w.getOsmuDrama() != null && w.getOsmuDrama() > 0).count();
        long anime = webtoons.stream().filter(w -> w.getOsmuAnime() != null && w.getOsmuAnime() > 0).count();
        long play = webtoons.stream().filter(w -> w.getOsmuPlay() != null && w.getOsmuPlay() > 0).count();
        long game = webtoons.stream().filter(w -> w.getOsmuGame() != null && w.getOsmuGame() > 0).count();
        long ox = webtoons.stream().filter(w -> w.getOsmuOX() != null && w.getOsmuOX() > 0).count();

        Map<String, Long> osmuRatio = new LinkedHashMap<>();
        osmuRatio.put("movie", movie);
        osmuRatio.put("drama", drama);
        osmuRatio.put("anime", anime);
        osmuRatio.put("play", play);
        osmuRatio.put("game", game);
        osmuRatio.put("ox", ox);

        return osmuRatio;
    }

    // 평균 평점 + 표준편차
    public Map<String, Double> getScoreStats() {
        List<Webtoon> webtoons = webtoonRepository.findAll();
        double avgStarScore = webtoons.stream()
                .filter(w -> w.getStarScore() != null)
                .mapToDouble(Webtoon::getStarScore)
                .average()
                .orElse(0.0);

        double stdDev = webtoons.stream()
                .filter(w -> w.getStarScore() != null)
                .mapToDouble(Webtoon::getStarScore)
                .toArray().length > 1 ?
                calculateStandardDeviation(
                        webtoons.stream()
                                .filter(w -> w.getStarScore() != null)
                                .mapToDouble(Webtoon::getStarScore)
                                .toArray(),
                        avgStarScore) : 0.0;

        Map<String, Double> result = new HashMap<>();
        result.put("averageStarScore", avgStarScore);
        result.put("starScoreStdDeviation", stdDev);

        return result;
    }

    private double calculateStandardDeviation(double[] scores, double mean) {
        double variance = 0.0;
        for (double score : scores) {
            variance += Math.pow(score - mean, 2);
        }
        variance /= (scores.length - 1);
        return Math.sqrt(variance);
    }

    // 전체 댓글 수
    public Long getTotalCommentCount() {
        return webtoonCommentRepository.count();
    }

    // 삭제된 댓글 비율
    public Double getDeletedCommentRatio() {
        long totalComments = webtoonCommentRepository.count();
        if (totalComments == 0) return 0.0;

        long deletedComments = webtoonCommentRepository.countByDeletedDateTimeIsNotNull();
        return (double) deletedComments / totalComments;
    }

}

