package k_webtoons.k_webtoons.log.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogRequestDto {
    private String type;        // ex: page_view, click
    private String page;        // 어떤 페이지에서 발생했는지
    private String target;      // 어떤 대상 클릭했는지 (선택)
    private int duration;       // 페이지 머문 시간 (초, 선택)
    private String keyword;     // 검색어 입력 등 (선택)
}