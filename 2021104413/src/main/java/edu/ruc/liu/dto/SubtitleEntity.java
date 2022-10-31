package edu.ruc.liu.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "better_call_saul")
@Data
public class SubtitleEntity {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private Integer season;

    @Field(type = FieldType.Keyword)
    private Integer episode;

    @Field(type = FieldType.Long)
    private Long startTime;

    @Field(type = FieldType.Long)
    private Long endTime;

    @Field(type = FieldType.Text)
    private String englishSub;

    @Field(type = FieldType.Text)
    private String chineseSub;

}
