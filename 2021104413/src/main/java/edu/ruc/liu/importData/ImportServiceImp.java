package edu.ruc.liu.importData;

import edu.ruc.liu.dto.SubtitleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;

@Service
@Slf4j
public class ImportServiceImp implements IImportService{


    @Override
    public void importIn(MultipartFile file){


    }

    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("/Users/tal/Documents/GitHub/middle22projects/2021104413/src/dataset/Better Call Saul S01E04 Hero 720p BluRay DTS x264-EbP.简体&英文.srt"));
            String str;
            ArrayList<SubtitleEntity> subtitleEntitieList = new ArrayList<>();
            while ((str = in.readLine()) != null) {
                if (subtitleEntitieList.size()>=100){
                    subtitleEntitieList = new ArrayList<>();
                }





                System.out.println(str);

            }
            System.out.println(str);
        } catch (IOException e) {
        }

    }
}
