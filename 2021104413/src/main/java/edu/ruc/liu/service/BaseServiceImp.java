package edu.ruc.liu.service;

import edu.ruc.liu.dto.SubtitleEntity;
import edu.ruc.liu.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class BaseServiceImp implements IBaseService{

    @Resource
    ISubtitleRepository subtitleRepository;

    @Override
    public List<SubtitleEntity> search(String word) {
        List<SubtitleEntity> subtitleEntityList;

        if (Util.containChinese(word)){
            subtitleEntityList = subtitleRepository.searchChineseSub(word);
            return subtitleEntityList;
        }else {
            subtitleEntityList = subtitleRepository.searchEnglishSub(word);
        }
        return subtitleEntityList;
    }

    @Override
    public List<SubtitleEntity> searchByTimeLine(Long time, String episode, String season) {
        List<SubtitleEntity> subtitleEntityList =subtitleRepository.searchByTimeLine(time,episode,season);


        return subtitleEntityList;
    }
}
