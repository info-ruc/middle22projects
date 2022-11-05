package org.imooc.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;
    public void tt(){
        //使用方式 set
        redisTemplate.opsForValue().set("key","value");
        //获取
        redisTemplate.opsForValue().get("key");
    }
}
