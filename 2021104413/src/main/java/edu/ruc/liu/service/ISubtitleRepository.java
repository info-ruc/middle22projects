package edu.ruc.liu.service;

import edu.ruc.liu.dto.SubtitleEntity;

import java.util.List;

public interface ISubtitleRepository {

    void saveBatch(List<SubtitleEntity> subtitleEntities);

    List<SubtitleEntity> searchChineseSub(String word);

    List<SubtitleEntity> searchByTimeLine(Long time, String episode, String season);

    List<SubtitleEntity> searchEnglishSub(String word);
}
