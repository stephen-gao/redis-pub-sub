package com.gs.redis;

import com.gs.RedisPubSubApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ...
 *
 * @author GaoSheng
 * @version 1.0
 * @blame GaoSheng
 * @since 2020/07/07 15:23
 **/
@SpringBootTest(classes = RedisPubSubApplication.class)
class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    void setKV() {
        redisService.setKV();
    }

    @Test
    void publishTest(){
        for (int i = 0; i < 10; i++) {
            redisService.publish();
        }
    }
}