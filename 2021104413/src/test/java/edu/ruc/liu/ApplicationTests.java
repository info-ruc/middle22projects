package edu.ruc.liu;

import edu.ruc.liu.importData.IImportService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
class ApplicationTests {

    @Resource
    IImportService importServiceImp;

    @Test
    void contextLoads() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("/Users/tal/Documents/GitHub/middle22projects/2021104413/src/dataset/Better Call Saul S01E04 Hero 720p BluRay DTS x264-EbP.简体&英文.srt"));
            String str;
            while ((str = in.readLine()) != null) {
                System.out.println(str);

            }
            System.out.println(str);
        } catch (IOException e) {
        }

    }

    @Test
    void process(){
        importServiceImp.process("/Users/tal/Documents/GitHub/middle22projects/2021104413/src/dataset/Better Call Saul S01E04 Hero 720p BluRay DTS x264-EbP.简体&英文.srt");


    }

}
