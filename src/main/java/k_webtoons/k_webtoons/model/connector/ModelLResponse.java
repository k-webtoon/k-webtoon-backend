package k_webtoons.k_webtoons.model.connector;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ModelLResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("titleId") Long titleId,
        @JsonProperty("titleName") String titleName,
        @JsonProperty("author") String author,
        @JsonProperty("adult") Boolean adult,
        @JsonProperty("age") String age,
        @JsonProperty("finish") Boolean finish,
        @JsonProperty("thumbnailUrl") String thumbnailUrl,
        @JsonProperty("synopsis") String synopsis,
        @JsonProperty("rankGenreTypes") List<String> rankGenreTypes,
        @JsonProperty("starScore") Double starScore,
        @JsonProperty("totalCount") Integer totalCount,
        @JsonProperty("sim") Integer similarity
) {
    // 추가: 기본 생성자 (필드명 일치를 위해 필요)
    public ModelLResponse(
            Long id,
            Long titleId,
            String titleName,
            String author,
            Boolean adult,
            String age,
            Boolean finish,
            String thumbnailUrl,
            String synopsis,
            List<String> rankGenreTypes,
            Double starScore,
            Integer totalCount,
            Integer similarity) {
        this.id = id;
        this.titleId = titleId;
        this.titleName = titleName;
        this.author = author;
        this.adult = adult;
        this.age = age;
        this.finish = finish;
        this.thumbnailUrl = thumbnailUrl;
        this.synopsis = synopsis;
        this.rankGenreTypes = rankGenreTypes;
        this.starScore = starScore;
        this.totalCount = totalCount;
        this.similarity = similarity;
    }
}