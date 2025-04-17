package k_webtoons.k_webtoons.initializer;

import k_webtoons.k_webtoons.repository.cosine_sim.CosineSimTableRepository;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.InputStream;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final WebtoonCsvImportService webtoonCsvImportService;
    private final WebtoonRepository webtoonRepository;
    private final CosineSimTableRepository cosineTableRepository;
    private final DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 1. 웹툰 CSV 데이터 먼저 로드
        if (webtoonRepository.count() == 0) {
            logger.info("웹툰 데이터를 CSV 파일에서 DB로 가져오는 중...");
            try (InputStream inputStream = new ClassPathResource("Dataset_add_cluster_character.csv").getInputStream()) {
                webtoonCsvImportService.saveWebtoonsFromCSV(inputStream); // ✅ 변경됨
                logger.info("✅ 웹툰 데이터 초기화 완료");
            }
        }

        // 2. 유저 & 리뷰 데이터 삽입 (기존 data.sql 수동 실행)
        executeSqlScript("data.sql");

        // 3. 유사도 데이터 로드 (기존 코드 유지)
        if (cosineTableRepository.count() == 0) {
            logger.info("유사도 데이터를 CSV 파일에서 DB로 가져오는 중...");

            try (InputStream inputStream = new ClassPathResource("cosine_sim_top10_tabel.csv").getInputStream()) {
                webtoonCsvImportService.saveWebtoonsFromCSV_2(inputStream); // ✅ 변경됨
                logger.info("✅ 유사도 데이터 초기화 완료");
            }
        }
    }


    // SQL 스크립트 실행 메서드 추가
    private void executeSqlScript(String scriptName) {
        try {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource(scriptName));
            populator.execute(dataSource);
            logger.info("✅ {} 실행 완료", scriptName);
        } catch (Exception e) {
            logger.error("❌ {} 실행 실패: {}", scriptName, e.getMessage());
        }
    }
}
