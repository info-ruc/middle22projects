package edu.ruc.liu.service;

import edu.ruc.liu.dto.SubtitleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class SubtitleRepositoryImp implements ISubtitleRepository{
    @Autowired
    private ElasticsearchRestTemplate template;


    @Override
    public void saveBatch(List<SubtitleEntity> subtitleEntities) {
        template.save(subtitleEntities);
    }
}
