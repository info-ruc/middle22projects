package edu.ruc.liu.importData;

import edu.ruc.liu.dto.SubtitleEntity;
import edu.ruc.liu.service.ISubtitleRepository;
import edu.ruc.liu.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;

@Service
@Slf4j
public class ImportServiceImp implements IImportService{
    @Autowired
    ISubtitleRepository subtitleRepositoryImp;

    @Override
    public void importIn(MultipartFile file){


    }


    public static void main(String[] args) {
        ImportServiceImp importServiceImp = new ImportServiceImp();
        importServiceImp.process("/Users/tal/Documents/GitHub/middle22projects/2021104413/src/dataset/Better Call Saul S01E04 Hero 720p BluRay DTS x264-EbP.简体&英文.srt");

        System.out.println();
    }
    @Override
    public void process(String fileName) {

        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String season = fileName.substring(fileName.indexOf("S0"),fileName.indexOf("S0")+3);
            String episode = fileName.substring(fileName.indexOf("S0")+3,fileName.indexOf("S0")+6);
            String str;
            ArrayList<SubtitleEntity> subtitleEntityList = new ArrayList<>();
            SubtitleEntity subtitleEntity = new SubtitleEntity(season,episode);
            int line=0;
            while ((str = in.readLine()) != null) {
                if (subtitleEntityList.size()>=100){
                    //do
                    subtitleRepositoryImp.saveBatch(subtitleEntityList);
                    //clear
                    subtitleEntityList.clear();
                }

                if (str.equals("")){
                    subtitleEntityList.add(subtitleEntity);
                    subtitleEntity = new SubtitleEntity(season,episode);
                    line=0;
                    continue;
                }

                switch (line){
                    case 0:
                        subtitleEntity.setId(season+"-"+episode+"-"+str);
                        break;
                    case 1:
                        subtitleEntity.setStartTime(getStartTime(str));
                        subtitleEntity.setEndTime(getEndTime(str));
                        break;
                    case 2:
                        subtitleEntity.setChineseSub(getChineseSub(str));
                        break;
                    case 3:
                        if (Util.containChinese(str)){
                            subtitleEntity.setChineseSub(subtitleEntity.getChineseSub()+str);
                            break;
                        }
                        subtitleEntity.setEnglishSub(str);
                        break;
                }
                line++;

//                subtitleEntityList.add(subtitleEntity);
            }
            subtitleRepositoryImp.saveBatch(subtitleEntityList);

        } catch (IOException e) {
        }

    }

    private String getChineseSub(String str) {
        return str.replace("{\\an1}","").replace("{\\an4}","").replace("{\\an8}","");
    }

    private Long getStartTime(String str) {
        String timeStr = str.substring(0, 12);
        return getTime(timeStr);
    }
    private Long getEndTime(String str) {
        String timeStr = str.substring(17, 29);
        return getTime(timeStr);
    }


    private Long getTime(String timeStr) {
        int hour = Integer.parseInt(timeStr.substring(0, 2));
        int minute = Integer.parseInt(timeStr.substring(3, 5));
        int second = Integer.parseInt(timeStr.substring(6, 8));
        int mSecond = Integer.parseInt(timeStr.substring(9, 12));
        return mSecond + second * 1000L + minute * 60 * 1000L + hour * 60 * 60 * 1000L;
    }
}
