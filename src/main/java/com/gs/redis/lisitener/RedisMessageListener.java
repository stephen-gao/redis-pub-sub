package com.gs.redis.lisitener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * ...
 *
 * @author GaoSheng
 * @version 1.0
 * @blame GaoSheng
 * @since 2020/07/07 15:57
 **/
public class RedisMessageListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("收到【{}】的消息：{}", new String(message.getChannel()), new String(message.getBody()));
    }
}
