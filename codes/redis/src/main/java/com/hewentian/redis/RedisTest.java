package com.hewentian.redis;

import com.hewentian.redis.util.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.*;

public class RedisTest {
    private static Jedis jedis;

    public static void main(String[] args) {
        try {
            jedis = RedisUtil.getJedis();

//            testString();
//            testMap();
//            testList();
//            testSet();
//            testZset();
//            testPool();
//            testSentinelPool();
//            testScan();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtil.close(jedis);
        }
    }

    private static void testString() {
        // 简单的key-value存储
        jedis.set("name", "Tim");
        System.out.println(jedis.get("name")); // Tim

        jedis.append("name", " Ho");
        jedis.append("age", "23");
        System.out.println(jedis.get("name")); // Tim Ho
        System.out.println(jedis.get("age")); // 23

        System.out.println(jedis.exists("name")); // true
        System.out.println(jedis.exists("sex")); // false

        // mset是设置多个key-value值，参数(key1, value1, key2, value2,..., keyn, valuen)
        // mget是获取多个key所对应的value，参数(key1, key2, key3, ..., keyn)返回的是个list
        jedis.mset("name1", "tim1", "name2", "tim2", "name3", "tim3");
        System.out.println(jedis.mget("name1", "name2", "name3")); // [tim1, tim2, tim3]
    }

    private static void testMap() {
        // map
        Map<String, String> user = new HashMap<>();
        user.put("name", "scott");
        user.put("password", "tiger");

        // map 存入redis
        jedis.hmset("user", user);
        jedis.hset("user", "age", "23");

        // mapkey个数
        System.out.println(String.format("len: %d", jedis.hlen("user"))); // len: 3

        // map中的所有键值
        System.out.println(String.format("keys: %s", jedis.hkeys("user"))); // keys: [name, password, age]

        // map中的所有value
        System.out.println(String.format("values: %s", jedis.hvals("user"))); // values: [tiger, scott, 23]

        // 取出map中的name、password字段的值
        List<String> userValues = jedis.hmget("user", "name", "password");
        System.out.println(userValues); // [scott, tiger]

        // 删除map中的某一个键值password
        jedis.hdel("user", "password");
        System.out.println(jedis.hmget("user", "name", "password")); // [scott, null]
    }

    private static void testList() {
        // list
        jedis.del("myList");
        System.out.println(jedis.lrange("myList", 0, -1)); // []

        jedis.lpush("myList", "A");
        jedis.lpush("myList", "B");
        jedis.lpush("myList", "C");

        System.out.println(jedis.lrange("myList", 0, -1)); // [C, B, A]
        System.out.println(jedis.lrange("myList", 0, 1)); // [C, B]
    }

    private static void testSet() {
        // set
        jedis.sadd("mySet", "h");
        jedis.sadd("mySet", "w");
        jedis.sadd("mySet", "t");
        jedis.sadd("mySet", "t");

        System.out.println(String.format("set num: %d", jedis.scard("mySet")));  // set num: 3
        System.out.println(String.format("all members: %s", jedis.smembers("mySet")));  // all members: [h, w, t]
        System.out.println(String.format("is member: %B", jedis.sismember("mySet", "h")));  // is member: TRUE
        System.out.println(String.format("rand member: %s", jedis.srandmember("mySet")));  // rand member: h

        // 删除一个对象
        jedis.srem("mySet", "t");
        System.out.println(String.format("all members: %s", jedis.smembers("mySet"))); // all members: [h, w]
    }

    private static void testZset() {
        // zset
        jedis.zadd("myZset", 0, "car");
        jedis.zadd("myZset", 2, "bike");

        List<String> sose = jedis.zrange("myZset", 0, -1);
        Iterator<String> it = sose.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + "\t"); // car bike
        }
    }

    private static void testPool() {
        Jedis jedis = RedisUtil.getJedisFromPool();
        jedis.select(0);

        jedis.set("name4", "tim");
        jedis.append("name4", " is a student.");

        System.out.println(jedis.get("name4")); // tim is a student.

        RedisUtil.close(jedis);
        RedisUtil.destroyPool();
    }

    // TODO 此方法，还未测试通过
    private static void testSentinelPool() {
        Jedis jedis = RedisUtil.getJedisFromSentinelPool();
        jedis.select(0);

        jedis.set("name5", "he");
        jedis.append("name5", " is a student.");

        System.out.println(jedis.get("name5")); // he is a student.

        RedisUtil.close(jedis);
        RedisUtil.destroySentinelPool();
    }

    public static void testScan() {
        // 先插入10个有相同前缀的 String key
        String keyPrefix = "H:W:T:SUCCESS_";

        for (int i = 0; i < 10; i++) {
            jedis.set(keyPrefix + i, "" + i);
        }

        ScanParams sp = new ScanParams();
        sp.match(keyPrefix + "*").count(5);

        ScanResult<String> scan;
        String cursor = "0";

        do {
            System.out.println("cursor: " + cursor);

            scan = jedis.scan(cursor, sp);
            cursor = scan.getCursor();

            for (String k : scan.getResult()) {
                String v = jedis.get(k);
                System.out.println("key: " + k + ", value: " + v);
            }
        } while (!"0".equals(cursor));

        System.out.println("\nend.");
    }
}
