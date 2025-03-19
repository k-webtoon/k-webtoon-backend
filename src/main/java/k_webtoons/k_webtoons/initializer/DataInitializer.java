package k_webtoons.k_webtoons.initializer;

import k_webtoons.k_webtoons.service.webtoon.WebtoonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private WebtoonService webtoonService;


    // webtoon.csv 파일 위치 자신의 파일 위치에 맞게 설정해주세요.
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String csvFilePath = "D:\\NAVER-Webtoon_OSMU.xlsx - final_finished_dataset.csv";
        webtoonService.saveWebtoonsFromCSV(csvFilePath);
    }
}