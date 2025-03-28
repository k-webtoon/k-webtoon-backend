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
public class WebtoonCsvImportService {

    @Autowired
    private WebtoonRepository webtoonRepository;

    private List<String> parseCsvList(String value) {
        if (!StringUtils.hasText(value)) return Collections.emptyList();
        return Arrays.stream(value
                        .replaceAll("[\\[\\]'\"]", "")
                        .split(",\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private String cleanTitleName(String titleName) {
        if (!StringUtils.hasText(titleName)) return "";
        return titleName.replace("[드라마원작]", "").trim();
    }

    private String cleanSynopsis(String synopsis) {
        if (!StringUtils.hasText(synopsis)) return "";
        return synopsis.replace("\n", " ").trim();
    }

    public void saveWebtoonsFromCSV(String csvFilePath) throws IOException, CsvException {
        RFC4180Parser parser = new RFC4180Parser();
        try (Reader reader = new FileReader(csvFilePath)) {
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(parser)
                    .build();

            String[] headers = csvReader.readNext();

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                Webtoon webtoon = Webtoon.builder()
                        .titleId(parseLong(nextLine[0]))
                        .titleName(cleanTitleName(nextLine[1]))
                        .original(parseBoolean(nextLine[2]))
                        .author(nextLine[3])
                        .artistId(nextLine[4])
                        .adult(parseBoolean(nextLine[5]))
                        .age(nextLine[6])
                        .finish(parseBoolean(nextLine[7]))
                        .thumbnailUrl(nextLine[8])
                        .synopsis(cleanSynopsis(nextLine[9]))
                        .genre(parseCsvList(nextLine[10]))
                        .rankGenreTypes(parseCsvList(nextLine[11]))
                        .tags(parseCsvList(nextLine[12]))
                        .totalCount(parseDouble(nextLine[13]))
                        .starScore(parseDouble(nextLine[14]))
                        .favoriteCount(parseDouble(nextLine[15]))
                        .starStdDeviation(parseDouble(nextLine[16]))
                        .likeMeanValue(parseDouble(nextLine[17]))
                        .likeStdDeviation(parseDouble(nextLine[18]))
                        .commentsMeanValue(parseDouble(nextLine[19]))
                        .commentsStdDeviation(parseDouble(nextLine[20]))
                        .collectedNumOfEpi(parseDouble(nextLine[21]))
                        .numOfWorks(parseDouble(nextLine[22]))
                        .numsOfWork2(parseDouble(nextLine[23]))
                        .writersFavorAverage(parseDouble(nextLine[24]))
                        .osmuMovie(parseInteger(nextLine[25]))
                        .osmuDrama(parseInteger(nextLine[26]))
                        .osmuAnime(parseInteger(nextLine[27]))
                        .osmuPlay(parseInteger(nextLine[28]))
                        .osmuGame(parseInteger(nextLine[29]))
                        .osmuOX(parseInteger(nextLine[30]))
                        .synopVec(parseVector(nextLine[31]))
                        .build();

                webtoonRepository.save(webtoon);
            }
        }
    }

    private Integer parseInteger(String value) {
        try {
            return StringUtils.hasText(value) ? (int) Double.parseDouble(value) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private float[] parseVector(String vectorString) {
        if (!StringUtils.hasText(vectorString)) return new float[0];
        String[] parts = vectorString.replaceAll("[\\[\\]]", "").trim().split("\\s+");
        float[] vector = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            vector[i] = Float.parseFloat(parts[i]);
        }
        return vector;
    }

    private Boolean parseBoolean(String value) {
        if (value == null) return false;
        return value.equals("1.0");
    }

    private Long parseLong(String value) {
        try {
            return StringUtils.hasText(value) ? (long) Double.parseDouble(value) : 0L;
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private Double parseDouble(String value) {
        try {
            return StringUtils.hasText(value) ? Double.parseDouble(value) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
