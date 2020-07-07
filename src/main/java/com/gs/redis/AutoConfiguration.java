package com.gs.redis;

import com.gs.redis.lisitener.RedisMessageListener;
import com.gs.redis.lisitener.SubcriberThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author GaoSheng
 * @version 1.0
 * @blame GaoSheng
 * @since 2020/07/07 14:52
 **/
@Configuration
public class AutoConfiguration {

    @Value("${redis.hosts}")
    private String hosts;

    @Value("${redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        List<RedisNode> redisNodeList = hostToRedisNode(hosts);
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setPassword(password);
        redisClusterConfiguration.setClusterNodes(redisNodeList);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration, jedisPoolConfig);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }


    @Bean
    public RedisTemplate redisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisConnection redisConnection(@Autowired RedisConnectionFactory redisConnectionFactory) {
        RedisConnection connection = redisConnectionFactory.getConnection();
        SubcriberThread subThread = new SubcriberThread(connection,new RedisMessageListener(),RedisKey.CHANNEL.getBytes());
        subThread.start();
        return connection;
    }


    /**
     * host 转hostNode对象
     *
     * @param host
     * @return
     */
    List<RedisNode> hostToRedisNode(String host) {

        String[] hostArr = host.split(",");

        if (hostArr == null || hostArr.length == 0) {
            throw new RuntimeException("host is blank");
        }

        List<RedisNode> redisNodeList = new ArrayList<>();

        try {
            for (String tempHost : hostArr) {
                String[] dataArr = tempHost.split(":");
                RedisNode redisNode = new RedisNode(dataArr[0], Integer.parseInt(dataArr[1]));
                redisNodeList.add(redisNode);
            }
        } catch (Exception e) {
            throw new RuntimeException("host format error, host:" + host);
        }


        return redisNodeList;
    }


}
