package com.cui.demo.pojo.entity;

import co.elastic.clients.util.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;

@Data
//@Table
@NoArgsConstructor
@AllArgsConstructor
public class UserEs {

//    private int id;
    private String user_name;
    private String password;
    private int role_id;
    private String img_url;
    private String province;
    private String city;
    private String address;
    private String telphone;
    private String email;
    private String real_name;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date update_time;
}
