package com.cui.demo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
//@Table
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "article")
public class EsArticle implements Serializable {

    private static final long serialVersionUID = 1L;

//    private String _id;
    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Long)
    private int user_id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Long)
    private int type;

    @Field(type = FieldType.Long)
    private int sort;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date create_time;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date update_time;
}
