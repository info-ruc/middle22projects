package edu.ruc.liu.importData;

import edu.ruc.liu.dto.SubtitleEntity;
import edu.ruc.liu.service.ISubtitleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;

@Service
@Slf4j
public class ImportServiceImp implements IImportService{
    @Resource
    ISubtitleRepository subtitleRepository;

    @Override
    public void importIn(MultipartFile file){


    }
    @Override
    public void process(String fileName) {

        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            ArrayList<SubtitleEntity> subtitleEntityList = new ArrayList<>();
            SubtitleEntity subtitleEntity = new SubtitleEntity();
            int line=0;
            while ((str = in.readLine()) != null) {
                if (subtitleEntityList.size()>=100){
                    //do
                    subtitleRepository.saveBatch(subtitleEntityList);
                    //clear
                    subtitleEntityList.clear();
                }



                if (str =="/n"){
                    subtitleEntity=new SubtitleEntity();
                    line=0;
                }




                subtitleEntityList.add(subtitleEntity);

            }
            System.out.println(str);
        } catch (IOException e) {
        }

    }
}
