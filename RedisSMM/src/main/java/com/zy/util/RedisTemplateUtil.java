package com.zy.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTemplateUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 操作String类型set
     */
    public void setString(String key, Object value){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
//设置有效时间 .expire()， TimeUnit.SECONDS为时间单位
        redisTemplate.expire(key, 1000, TimeUnit.SECONDS);

    }

    /**
     * 操作String类型get
     */
    public Object getString(String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 操作List链表类型的set/get
     */
    public void setList(String key, List<?> value) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        listOperations.leftPush(key, value);
    }
    public Object getList(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 操作set集合类型的set/get
     */
    public void setSet(String key, Set<?> value) {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.add(key, value);
    }
    public Object getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 操作hash对象类型的set/get
     */
    public void setHash(String key, Map<String, ?> value) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, value);
    }
    public Object getHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 根据Key键删除数据（针对String、Hash、List、Set等类型）
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
