package com.hewentian.redis.util;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.IOException;

public class RedissonUtils {
    private static RedissonClient redissonClient = null;
    private static String lockKey = "lockKey";

    public static RedissonClient getRedissonClient() {
        if (null == redissonClient) {
            synchronized (lockKey) {
                if (null == redissonClient) {
                    System.out.println("creating redissonClient...");

                    try {
                        Config config = Config.fromYAML(RedissonUtils.class.getClassLoader().getResourceAsStream("redisson.yml"));

                        redissonClient = Redisson.create(config);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return redissonClient;
    }

    public static void shutdown(RedissonClient redissonClient) {
        System.out.println("shutdown redissonClient...");
        redissonClient.shutdown();
    }

}
