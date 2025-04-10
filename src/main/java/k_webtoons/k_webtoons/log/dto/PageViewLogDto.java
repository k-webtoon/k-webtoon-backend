package k_webtoons.k_webtoons.log.dto;

import lombok.Data;

@Data
public class PageViewLogDto {
    private String page;
    private Integer duration;
}
