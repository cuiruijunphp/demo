package com.cui.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cui.demo.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
