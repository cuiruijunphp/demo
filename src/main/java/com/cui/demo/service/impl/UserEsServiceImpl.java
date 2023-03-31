package com.cui.demo.service.impl;

import com.cui.demo.mapper.UserEsMapper;
import com.cui.demo.pojo.entity.UserEs;
import com.cui.demo.service.UserEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserEsServiceImpl implements UserEsService {

    @Autowired
    private UserEsMapper userEsMapper;

    @Override
    public int insert(List<UserEs> user) {
        return userEsMapper.insertData(user);
    }
}
