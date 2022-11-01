package edu.ruc.liu.service;

import edu.ruc.liu.dto.SubtitleEntity;
import edu.ruc.liu.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseServiceImp implements IBaseService{

    @Override
    public SubtitleEntity search(String word) {
        if (Util.containChinese(word)){
        }
        return null;
    }
}
