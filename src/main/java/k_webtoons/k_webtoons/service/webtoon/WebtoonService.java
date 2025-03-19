package k_webtoons.k_webtoons.service.webtoon;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.exceptions.CsvException;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WebtoonService {

    @Autowired
    private WebtoonRepository webtoonRepository;

    // CSV 리스트 파싱 메서드
    private List<String> parseCsvList(String value) {
        if (!StringUtils.hasText(value)) return Collections.emptyList();

        return Arrays.stream(value
                        .replaceAll("[\\[\\]']", "")  // 대괄호 및 작은따옴표 제거
                        .split(",\\s*"))              // 쉼표+공백 기준 분리
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public void saveWebtoonsFromCSV(String csvFilePath) throws IOException, CsvException {
        RFC4180Parser parser = new RFC4180Parser();
        try (Reader reader = new FileReader(csvFilePath)) {
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(parser)
                    .build();

            String[] headers = csvReader.readNext(); // 헤더 추출

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                Webtoon webtoon = Webtoon.builder()
                        .titleId(parseLong(nextLine[0]))
                        .titleName(nextLine[1])
                        .original(parseBoolean(nextLine[2]))
                        .author(nextLine[3])
                        .artistId(nextLine[4])
                        .adult(parseBoolean(nextLine[5]))
                        .age(nextLine[6])
                        .finish(parseBoolean(nextLine[7]))
                        .thumbnailUrl(nextLine[8])
                        .synopsis(nextLine[9])
                        .genre(parseCsvList(nextLine[10]))
                        .rankGenreTypes(parseCsvList(nextLine[11]))
                        .tags(parseCsvList(nextLine[12]))
                        .totalCount(parseLong(nextLine[13]))
                        .viewCount(parseLong(nextLine[14]))
                        .starScore(parseDouble(nextLine[15]))
                        .favoriteCount(parseLong(nextLine[16]))
                        .starStdDeviation(parseDouble(nextLine[17]))
                        .likeMeanValue(parseDouble(nextLine[18]))
                        .likeStdDeviation(parseDouble(nextLine[19]))
                        .commentsMeanValue(parseDouble(nextLine[20]))
                        .commentsStdDeviation(parseDouble(nextLine[21]))
                        .collectedNumOfEpi(parseInteger(nextLine[22]))
                        .numOfWorks(parseInteger(nextLine[23]))
                        .numsOfWork2(parseInteger(nextLine[24]))
                        .writersFavorAverage(parseDouble(nextLine[25]))
                        .osmuMovie(parseInteger(nextLine[26]))
                        .osmuDrama(parseInteger(nextLine[27]))
                        .osmuAnime(parseInteger(nextLine[28]))
                        .osmuPlay(parseInteger(nextLine[29]))
                        .osmuGame(parseInteger(nextLine[30]))
                        .osmuOX(parseInteger(nextLine[31]))
                        .build();

                webtoonRepository.save(webtoon);
            }
        }
    }

    // Helper methods
    private Boolean parseBoolean(String value) {
        return "TRUE".equalsIgnoreCase(value) || "1".equals(value);
    }

    private Long parseLong(String value) {
        if (!StringUtils.hasText(value)) return 0L;
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private Integer parseInteger(String value) {
        if (!StringUtils.hasText(value)) return 0;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Double parseDouble(String value) {
        if (!StringUtils.hasText(value)) return 0.0;
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
