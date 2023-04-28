package com.cui.demo.pojo.dto;

import co.elastic.clients.util.DateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Test1Param {
    private Integer id;
    private String user_name;
    private String title;
    private String content;
    private String type;
}
