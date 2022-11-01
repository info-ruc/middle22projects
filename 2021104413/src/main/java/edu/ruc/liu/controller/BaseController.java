package edu.ruc.liu.controller;

import edu.ruc.liu.dto.Response;
import edu.ruc.liu.dto.SubtitleEntity;
import edu.ruc.liu.importData.IImportService;
import edu.ruc.liu.service.IBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Slf4j
@RestController
public class BaseController {

    @Resource
    IImportService importService;

    @Resource
    IBaseService baseService;

    @PostMapping("/import")
    public Response importData(MultipartFile file){


        importService.importIn(file);
        return null;

    }

//    @GetMapping("/search")
//    public Response<SubtitleEntity> search(String word){
//        SubtitleEntity subtitleEntity = baseService.search(word);
//        return new Response<>(subtitleEntity);
//    }

}
