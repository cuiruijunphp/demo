package com.cui.demo.controller;

import co.elastic.clients.json.JsonpUtils;
import com.cui.demo.pojo.dto.UserDto;
import com.cui.demo.service.UserService;
import com.cui.demo.util.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;


@RestController
public class User {
    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping("/register")
    public void register(UserDto userDto){
        userService.register(userDto);
    }

    @ResponseBody
    @PostMapping("/login")
    public com.cui.demo.pojo.entity.User login(UserDto userDto){
        com.cui.demo.pojo.entity.User user = userService.login(userDto);
        return user;
    }

    @ResponseBody
    @PostMapping("/register_common")
    public void registerCommon(HttpServletRequest httpServletRequest) throws ParseException {

        Map<String, String> map = HttpRequestUtil.commonHttpRequestParam(httpServletRequest);
        UserDto userDto = new UserDto();
        userDto.setUser_name(map.get("user_name"));
        userDto.setPassword(map.get("password"));
        userDto.setEmail(map.get("email"));
        userDto.setType(Integer.valueOf(map.get("type")));

        String date = map.get("birthday");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        userDto.setBirthday(df.parse(date));

        userService.register(userDto);
    }
}
