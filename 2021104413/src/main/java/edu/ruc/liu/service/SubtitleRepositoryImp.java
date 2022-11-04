package edu.ruc.liu.service;

import edu.ruc.liu.dto.SubtitleEntity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.AggregationContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Component
public class SubtitleRepositoryImp implements ISubtitleRepository{
    @Autowired
    private ElasticsearchRestTemplate template;


    @Override
    public void saveBatch(List<SubtitleEntity> subtitleEntities) {
        template.save(subtitleEntities);
        log.info("done");
    }

    @Override
    public List<SubtitleEntity> searchChineseSub(String word) {
         NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                 .withQuery(QueryBuilders.matchQuery("chineseSub", word))
                 .build();
         SearchHits<SubtitleEntity> searchHits =  template.search(
                 nativeSearchQuery, SubtitleEntity.class);
         List<SubtitleEntity> SubtitleEntityList = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
         return SubtitleEntityList;
    }

    @Override
    public List<SubtitleEntity> searchByTimeLine(Long time, String episode, String season) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchPhraseQuery("episode", episode))
                        .must(QueryBuilders.matchPhraseQuery("season", season))
                        .must(QueryBuilders.rangeQuery("startTime").lte(time))
                        .must(QueryBuilders.rangeQuery("endTime").gte(time))
                ).build();
        SearchHits<SubtitleEntity> searchHits =  template.search(
                nativeSearchQuery, SubtitleEntity.class);
        List<SubtitleEntity> SubtitleEntityList = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());



        return SubtitleEntityList;
    }

    @Override
    public List<SubtitleEntity> searchEnglishSub(String word) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("english", word))
                .build();
        SearchHits<SubtitleEntity> searchHits =  template.search(
                nativeSearchQuery, SubtitleEntity.class);
        List<SubtitleEntity> SubtitleEntityList = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        return SubtitleEntityList;

    }
}
