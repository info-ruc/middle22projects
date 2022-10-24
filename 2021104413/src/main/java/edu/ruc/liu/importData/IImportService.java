package edu.ruc.liu.importData;

import org.springframework.web.multipart.MultipartFile;

public interface IImportService {
    void importIn(MultipartFile file);
}
