package k_webtoons.k_webtoons.initializer;

import k_webtoons.k_webtoons.repository.cosine_sim.CosineSimTableRepository;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private WebtoonCsvImportService webtoonCsvImportService;

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Autowired
    private CosineSimTableRepository cosineTableRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 웹툰 데이터 초기화
        if (webtoonRepository.count() > 0) {
            logger.info("웹툰 데이터가 이미 존재합니다. CSV 파일을 불러오지 않습니다.");
        } else {
            String webtoonCsvFile = "D:/Dataset_add_cluster_character.csv";
            logger.info("웹툰 데이터를 CSV 파일에서 DB로 가져오는 중...");
            webtoonCsvImportService.saveWebtoonsFromCSV(webtoonCsvFile);
            logger.info("웹툰 데이터 초기화 완료.");
        }

        // 유사도 데이터 초기화
        if (cosineTableRepository.count() > 0) {
            logger.info("유사도 데이터가 이미 존재합니다. CSV 파일을 불러오지 않습니다.");
        } else {
            String similarityCsvFile = "D:/cosine_sim_top10_tabel.csv";
            logger.info("유사도 데이터를 CSV 파일에서 DB로 가져오는 중...");
            webtoonCsvImportService.saveWebtoonsFromCSV_2(similarityCsvFile);
            logger.info("유사도 데이터 초기화 완료.");
        }
    }
}