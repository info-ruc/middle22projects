package edu.ruc.liu.importData;

import edu.ruc.liu.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Slf4j
@RestController
public class BaseController {

    @Resource
    IImportService importService;

    @PostMapping("/import")
    public Response importData(MultipartFile file){


        importService.importIn(file);
        return null;

    }
}
