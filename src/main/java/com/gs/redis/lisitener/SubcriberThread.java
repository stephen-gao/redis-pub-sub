package com.gs.redis.lisitener;

import org.springframework.data.redis.connection.RedisConnection;

/**
 * ...
 *
 * @author GaoSheng
 * @version 1.0
 * @blame GaoSheng
 * @since 2020/07/07 16:44
 **/
public class SubcriberThread extends Thread {

    private RedisConnection connection;

    private RedisMessageListener subcriber;

    private byte[] channel;

    public SubcriberThread(RedisConnection connection, RedisMessageListener subcriber, byte[] channel) {
        super();
        this.connection = connection;
        this.subcriber = subcriber;
        this.channel = channel;
    }

    @Override
    public void run() {
        connection.subscribe(subcriber, channel);
    }
}
