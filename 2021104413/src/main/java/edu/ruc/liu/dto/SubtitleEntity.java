package edu.ruc.liu.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "S01_better_call_saul")
@Data
public class SubtitleEntity {

    @Id
    private String number;

    @Field(type = FieldType.Long)
    private String startTime;

    @Field(type = FieldType.Long)
    private String endTime;

    @Field(type = FieldType.Text)
    private String englishSub;

    @Field(type = FieldType.Text)
    private String chineseSub;

}
