package com.gs.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * ...
 *
 * @author GaoSheng
 * @version 1.0
 * @blame GaoSheng
 * @since 2020/07/07 14:38
 **/
@Service
public class RedisService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisConnection redisConnection;

    public void setKV() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(RedisKey.TEST_KEY, "test00001111");
    }


    public void publish() {
        String message = "i am chinese, i love china";
        redisConnection.publish(RedisKey.CHANNEL.getBytes(), message.getBytes());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("afterPropertiesSet");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = "i am chinese, i love china";
                logger.info("发送订阅消息：{}", message);
                redisConnection.publish(RedisKey.CHANNEL.getBytes(), message.getBytes());
            }
        });
        thread.start();
    }
}
