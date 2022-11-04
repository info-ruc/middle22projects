package edu.ruc.liu.controller;

import edu.ruc.liu.dto.Response;
import edu.ruc.liu.dto.SubtitleEntity;
import edu.ruc.liu.importData.IImportService;
import edu.ruc.liu.service.IBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

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
        return Response.success(null);

    }

    @GetMapping("/searchSub")
    public Response<List<SubtitleEntity>> search(@RequestParam String word){
        List<SubtitleEntity> search = baseService.search(word);
        return Response.success(search);
    }

    @GetMapping("/searchByTimeLine")
    public Response<List<SubtitleEntity>> searchByTimeLine(@RequestParam Long time,
                                                           @RequestParam String episode,
                                                           @RequestParam String season){
        List<SubtitleEntity> search = baseService.searchByTimeLine(time,episode,season);
        return Response.success(search);
    }


}
