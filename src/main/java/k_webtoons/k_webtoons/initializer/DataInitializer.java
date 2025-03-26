package k_webtoons.k_webtoons.initializer;

import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import k_webtoons.k_webtoons.service.webtoon.WebtoonCsvImportService;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (webtoonRepository.count() > 0) {
            logger.info("웹툰 데이터가 이미 존재합니다. CSV 파일을 불러오지 않습니다.");
            return;  // DB에 데이터가 있으면 CSV 파일을 불러오지 않고 종료
        }

        String csvFilePath = "D:\\NAVER-Webtoon_OSMU.xlsx - final_finished_dataset.csv";
        logger.info("웹툰 데이터를 CSV 파일에서 DB로 가져오는 중...");
        webtoonCsvImportService.saveWebtoonsFromCSV(csvFilePath);
        logger.info("웹툰 데이터 초기화 완료.");
    }
}