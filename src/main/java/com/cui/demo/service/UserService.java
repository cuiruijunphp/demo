package com.cui.demo.service;


import com.cui.demo.pojo.dto.UserDto;
import com.cui.demo.pojo.entity.User;
import com.cui.demo.pojo.entity.UserEs;

import java.util.List;

public interface UserService {
    int register(UserDto user);
    User login(UserDto userDto);
}
