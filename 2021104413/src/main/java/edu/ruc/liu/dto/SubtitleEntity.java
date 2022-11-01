package edu.ruc.liu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "better_call_saul")
@Data
@NoArgsConstructor
public class SubtitleEntity {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String season;

    @Field(type = FieldType.Keyword)
    private String episode;

    @Field(type = FieldType.Long)
    private Long startTime;

    @Field(type = FieldType.Long)
    private Long endTime;

    @Field(type = FieldType.Text)
    private String englishSub;

    @Field(type = FieldType.Text)
    private String chineseSub;


    public SubtitleEntity(String season, String episode) {
        this.season = season;
        this.episode = episode;
    }
}
