package com.cui.demo.mapper;

import com.cui.demo.pojo.entity.UserEs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserEsMapper {
    int insertData(List<UserEs> userList);
}
