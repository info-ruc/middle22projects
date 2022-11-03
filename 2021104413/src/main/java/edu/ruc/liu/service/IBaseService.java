package edu.ruc.liu.service;

import edu.ruc.liu.dto.SubtitleEntity;

import java.util.List;

public interface IBaseService {
    List<SubtitleEntity> search(String word);

    List<SubtitleEntity> searchByTimeLine(Long time, String episode, String season);
}
