package edu.ruc.liu;

import edu.ruc.liu.importData.IImportService;
import edu.ruc.liu.service.ISubtitleRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
class ApplicationTests {

    @Resource
    IImportService importServiceImp;

    @Resource
    ISubtitleRepository subtitleRepository;

    @Test
    void search(){


        subtitleRepository.searchChineseSub("吉米");
    }




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

        String path ="/Users/tal/Documents/GitHub/middle22projects/2021104413/src/dataset/Season1";
        String[] fileList = new File(path).list();
        for (String s : fileList) {
            importServiceImp.process(path+"/"+s);
        }
        String path2 ="/Users/tal/Documents/GitHub/middle22projects/2021104413/src/dataset/Season2";
        String[] fileList2 = new File(path2).list();
        for (String s : fileList2) {
            importServiceImp.process(path+"/"+s);
        }

    }

    public static void main(String[] args) {
        String[] list = new File("/Users/tal/Documents/GitHub/middle22projects/2021104413/src/dataset").list();
        Arrays.stream(list).forEach(System.out::println);
    }

}
