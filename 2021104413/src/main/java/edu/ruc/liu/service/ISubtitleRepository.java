package edu.ruc.liu.service;

import edu.ruc.liu.dto.SubtitleEntity;

import java.util.List;

public interface ISubtitleRepository {

    void saveBatch(List<SubtitleEntity> subtitleEntities);

}
