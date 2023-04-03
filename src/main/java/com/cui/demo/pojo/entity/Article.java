package com.cui.demo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
//@Table
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    private Long id;
    private int user_id;
    private String title;
    private int type;
    private int sort;
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date update_time;
}
