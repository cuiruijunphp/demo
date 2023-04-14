package com.cui.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Objects;
import java.util.regex.Matcher;

@Configuration
public class RedisConfig {
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(factory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringRedisSerializer); // key的序列化类型

        GenericJackson2JsonRedisSerializerCustomized jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializerCustomized();
//        Jackson2JsonRedisSerializer  jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 方法过期，改为下面代码
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance ,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); // value的序列化类型
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}

//解决string类型数据会多个双引号的问题
class GenericJackson2JsonRedisSerializerCustomized extends GenericJackson2JsonRedisSerializer {
    @Override
    public byte[] serialize(Object source) throws SerializationException {
        if (Objects.nonNull(source)) {
            if (source instanceof String || source instanceof Character) {
                return source.toString().getBytes();
            }
        }
        return super.serialize(source);
    }
    @Override
    public <T> T deserialize(byte[] source, Class<T> type) throws SerializationException {
        //反序列化时，如果不加双引号会报解析不了json的错
        String source_tmp = new String(source);

        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(source_tmp.replaceAll("\"", Matcher.quoteReplacement("\\\"")));
        sb.append("\"");
        source_tmp = sb.toString();

        byte[] source2 = source_tmp.getBytes();
        return super.deserialize(source2, type);
    }
}
