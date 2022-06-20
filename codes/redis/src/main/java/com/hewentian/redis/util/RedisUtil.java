package com.hewentian.redis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RedisUtil {
    private static String host;
    private static int port;
    private static String password;
    private static int database;

    private static JedisPool jedisPool;
    private static JedisSentinelPool jedisSentinelPool;

    static {
        try {
            host = Config.get("redis.host", "localhost");
            port = Integer.valueOf(Config.get("redis.port", "6379"));
            password = Config.get("redis.password", "");
            database = Integer.valueOf(Config.get("redis.database", "0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Jedis getJedis() {
        Jedis jedis = new Jedis(host, port);

        // 密码验证，如果你没有设置redis密码可不验证
        jedis.auth(password);

        jedis.select(database); // 也可以在使用的时候再选择哪一个库

        return jedis;
    }

    public static Jedis getJedisFromPool() {
        if (null == jedisPool) {
            jedisPool = new JedisPool(getJedisPoolConfig(), host, port);
        }

        Jedis jedis = jedisPool.getResource();
        jedis.auth(password);

        return jedis;
    }

    public static Jedis getJedisFromSentinelPool() {
        if (null == jedisSentinelPool) {
            String master = Config.get("redis.sentinel.master", null);
            String nodes = Config.get("redis.sentinel.nodes", null);
            String password = Config.get("redis.sentinel.password", null);

            jedisSentinelPool = new JedisSentinelPool(
                    master,
                    Arrays.stream(nodes.split(",")).collect(Collectors.toSet()),
                    getJedisPoolConfig(),
                    password);
        }

        Jedis jedis = jedisSentinelPool.getResource();
        jedis.auth(password);

        return jedis;
    }

    private static JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(Integer.valueOf(Config.get("redis.maxTotal", "16")));
        jedisPoolConfig.setMinIdle(Integer.valueOf(Config.get("redis.minIdle", "4")));
        jedisPoolConfig.setMaxIdle(Integer.valueOf(Config.get("redis.maxIdle", "16")));
        jedisPoolConfig.setMaxWaitMillis(Long.valueOf(Config.get("redis.maxWaitMillis", "30000")));
        jedisPoolConfig
                .setMinEvictableIdleTimeMillis(Long.valueOf(Config.get("redis.minEvictableIdleTimeMillis", "60000")));
        jedisPoolConfig.setNumTestsPerEvictionRun(Integer.valueOf(Config.get("redis.numTestsPerEvictionRun", "-1")));
        jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(
                Long.valueOf(Config.get("redis.softMinEvictableIdleTimeMillis", "-1")));
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(
                Long.valueOf(Config.get("redis.timeBetweenEvictionRunsMillis", "30000")));
        jedisPoolConfig.setBlockWhenExhausted(Boolean.valueOf(Config.get("redis.blockWhenExhausted", "true")));

        return jedisPoolConfig;
    }

    public static void close(Jedis jedis) {
        try {
            jedis.disconnect();
            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroyPool() {
        try {
            jedisPool.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroySentinelPool() {
        try {
            jedisSentinelPool.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
