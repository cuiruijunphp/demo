package com.cui.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cui.demo.mapper.UserMapper;
import com.cui.demo.pojo.dto.UserDto;
import com.cui.demo.pojo.entity.User;
import com.cui.demo.service.UserService;
import com.cui.demo.util.RedisUtil;
import com.cui.demo.util.StringDealUtil;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

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
        String user_key = "user_info_" + user_id;
        if(!redisUtil.hasKey(user_key)){
            //将登录信息写入到redis
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Map<String, Object> redisMap = new HashMap<>();
            redisMap.put("userName", user.getUserName());
            redisMap.put("email", user.getEmail());
            redisMap.put("type", String.valueOf(user.getType()));
//            redisMap.put("birthday", df.format(user.getBirthday()));
//            redisMap.put("@class", "User");
//            logger.info("写入redis" + redisMap.toJSONString());
//            redisTemplate.opsForValue().set(String.valueOf(user.getId()), redisMap.toJSONString());
//            RedisUtil redisUtil = new RedisUtil();
//            redisUtil.set(user_id, redisMap.toJSONString());
            redisUtil.hput(user_key, "userName", user.getUserName());
            redisUtil.hput(user_key, "email", user.getEmail());

            redisUtil.hPutAll("user_info_all_" + user_id, redisMap);
        }

        Map<Object, Object> hashEntries = redisUtil.getHashEntries(user_key);
        System.out.println(redisUtil.getHashEntries(user_key));

        Object tt = redisUtil.hGet(user_key, "userName");
        System.out.println(tt);
        Map<String, Object> hash_all = redisUtil.hGetAll(user_key);
        System.out.println(hash_all);
        for (Map.Entry<String, Object> entry: hash_all.entrySet()){
            System.out.println("---" + entry.getKey() + ":::" + entry.getValue());
        }
        System.out.println(hash_all.get("userName"));

        //list格式
//        String list_key = "list_test";
//        redisUtil.leftPush(list_key, 11222);
//        redisUtil.leftPush(list_key, "hello");
////        redisUtil.leftPush(list_key, hash_all);
//
//        System.out.println(redisUtil.range(list_key, 0 , -1));

        //zset格式
        Long time = System.currentTimeMillis()/1000;
        String zset_key = "zset_test";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_name", "xiaoliang");
        jsonObject.put("address", "xixian");
        jsonObject.put("province", "henan");
        jsonObject.put("time", time);
        String json_content = jsonObject.toJSONString();
        redisTemplate.opsForZSet().add(zset_key, json_content, time);

        Long maxNum = 9999999999L;
        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().rangeByScoreWithScores(zset_key, 0, maxNum);
        for (ZSetOperations.TypedTuple<String> value1: set){
            String val = value1.getValue();
            System.out.println("----" + val);
            JSONObject jsonObject1 = JSONObject.parseObject(val);
            System.out.println("这是json格式的name:" + jsonObject1.get("user_name"));
        }

        return user;
    }
}
