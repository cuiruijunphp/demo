package com.cui.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cui.demo.mapper.UserMapper;
import com.cui.demo.pojo.dto.UserDto;
import com.cui.demo.pojo.entity.User;
import com.cui.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Resource
    private RedisTemplate redisTemplate;

    protected static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public int register(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        //设置成当前时间
        Long current_time = System.currentTimeMillis();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        df.parse()  传过来的字符串
        //将密码md5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        //设置时间为当前时间
        user.setCreateTime(new Date(current_time));
        user.setUpdateTime(new Date(current_time));
        return userMapper.insert(user);
    }

    @Override
    public User login(UserDto userDto) {
        if(StringUtils.isEmpty(userDto.getUserName()) || StringUtils.isEmpty(userDto.getPassword())){
            //不能为空
            logger.error("不能为空");
        }
        String password = DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes());

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().select().eq(User::getUserName,userDto.getUserName())
                .eq(User::getPassword, password).last(" limit 1"));

        logger.info("user为:" + user.toString());

        String user_id=String.valueOf(user.getId());
        if(StringUtils.isEmpty(redisTemplate.opsForValue().get(user_id))){
            //将登录信息写入到redis
            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

            //设置key和value序列化方式
            redisTemplate.setKeySerializer(stringRedisSerializer);
            redisTemplate.setValueSerializer(stringRedisSerializer);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            JSONObject redisMap = new JSONObject();
            redisMap.put("userName", user.getUserName());
            redisMap.put("email", user.getEmail());
            redisMap.put("type", String.valueOf(user.getType()));
            redisMap.put("birthday", df.format(user.getBirthday()));
            logger.info("写入redis");
            redisTemplate.opsForValue().set(String.valueOf(user.getId()), redisMap.toJSONString());
        }

        return user;
    }
}
