package com.cui.demo.pojo.dto;

import co.elastic.clients.util.DateTime;
import com.fasterxml.jackson.annotation.JsonInclude;

public class UserEsParam {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String user_name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String password;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int role_id;
    private String img_url;
    private String province;
    private String city;
    private String address;
    private String telphone;
    private String email;
    private DateTime create_time;
    private DateTime update_time;
}
