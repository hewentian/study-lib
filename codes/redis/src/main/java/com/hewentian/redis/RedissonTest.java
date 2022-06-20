package com.hewentian.redis;

import com.hewentian.redis.entity.User;
import com.hewentian.redis.entity.UserLiveObject;
import com.hewentian.redis.service.UserService;
import com.hewentian.redis.service.impl.UserServiceImpl;
import com.hewentian.redis.util.RedissonUtils;
import org.redisson.RedissonRedLock;
import org.redisson.api.*;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisClientConfig;
import org.redisson.client.RedisConnection;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.RedisCommands;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RedissonTest {
    private static RedissonClient redissonClient;

    public static void main(String[] args) {
        try {
            redissonClient = RedissonUtils.getRedissonClient();

//            testRAtomicLong();
//            testKeys();
//            testBucket();
//            testTopic();
//            testMap();
//            testSet();
//            testList();
//            testRemoteService();
//            testLiveObjectService();
//            testPipeline();
//            testScript();
//            testLowLevelClient();
//            testLock();
//            testRedLock();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedissonUtils.shutdown(redissonClient);
        }
    }

    static void testRAtomicLong() {
        RAtomicLong myLong = redissonClient.getAtomicLong("myLong");
        myLong.set(1);
        myLong.incrementAndGet();

        RFuture<Boolean> future = myLong.compareAndSetAsync(2, 8);
        future.handle((result, exception) -> {
            if (result.booleanValue()) {
                System.out.println("set success");
            } else {
                System.out.println("set false");
            }

            return result;
        });
    }

    static void testKeys() {
        RKeys keys = redissonClient.getKeys();

        System.out.println("output all keys");
        keys.getKeys().forEach(e -> System.out.println(e));

        System.out.println("output keys by pattern");
        keys.getKeysByPattern("my*").forEach(e -> System.out.println(e));
    }

    static void testBucket() {
        RBucket<User> userRBucket = redissonClient.getBucket("user");
        userRBucket.set(User.builder().id(1).name("scott").age(20).birthday(new Date()).address("Guangzhou").createTime(new Date()).build());

        User user = userRBucket.get();
        System.out.println(user);
    }

    static void testTopic() {
        // 先订阅
        RTopic subscribeTopic = redissonClient.getTopic("myTopic");
        subscribeTopic.addListener(User.class, ((channel, msg) -> {
            System.out.println("收到消息：" + msg);
        }));

        // 然后发布消息
        RTopic publicTopic = redissonClient.getTopic("myTopic");
        publicTopic.publish(User.builder().id(1000).name("scott").age(20).build());
    }

    static void testMap() {
        RMap<Integer, User> userRMap = redissonClient.getMap("userMap");
        userRMap.put(1000, User.builder().id(1000).name("scott").age(20).build());

        RMap<Integer, User> userRMap2 = redissonClient.getMap("userMap");
        User user = userRMap2.get(1000);
        System.out.println(user);
    }

    static void testSet() {
        RSet<User> userRSet = redissonClient.getSet("userSet");
        userRSet.add(User.builder().id(1000).name("scott").age(20).build());
    }

    static void testList() {
        RList<User> userRList = redissonClient.getList("userList");
        userRList.add(User.builder().id(1000).name("scott").age(20).build());
    }

    static void testRemoteService() {
        // mock as server side
        RRemoteService serverService = redissonClient.getRemoteService();
        serverService.register(UserService.class, new UserServiceImpl());

        // mock as client side
        RRemoteService clientService = redissonClient.getRemoteService();
        UserService userService = clientService.get(UserService.class);
        User user = userService.findById(1000);
        System.out.println(user);
    }

    static void testLiveObjectService() {
        // mock client1: to save object
        RLiveObjectService service = redissonClient.getLiveObjectService();
        UserLiveObject userLiveObject = UserLiveObject.builder().id(1000).name("scott").age(20).build();
        service.persist(userLiveObject);

        // mock cient2: to get object
        // get our Live Object using the field annotated with @RId
        RLiveObjectService service2 = redissonClient.getLiveObjectService();
        UserLiveObject userLiveObject2 = service2.get(UserLiveObject.class, 1000);
        System.out.println(userLiveObject2);
    }

    static void testPipeline() {
        // multiple operations can be batched as a single atomic operation
        RBatch batch = redissonClient.createBatch();

        batch.getBucket("name").setAsync("scott");
        batch.getMap("userMap").putAsync(1002, User.builder().id(1002).name("tiger").age(22).build());

        BatchResult<?> result = batch.execute();
        System.out.println(result.getResponses());
    }

    static void testScript() {
        redissonClient.getBucket("name").set("tim");

        String luaScript = "return redis.call('get', 'name')";
        String value = redissonClient.getScript().eval(RScript.Mode.READ_ONLY, luaScript, RScript.ReturnType.VALUE);
        System.out.println(value);
    }

    static void testLowLevelClient() {
        // 用于执行一些 redisson 还未支持的 redis 操作，即执行 redis 原生操作
        RedisClientConfig redisClientConfig = new RedisClientConfig();
        redisClientConfig.setAddress("redis.hewentian.com", 6379);
        redisClientConfig.setPassword("abc123");
        redisClientConfig.setDatabase(0);

        RedisClient redisClient = RedisClient.create(redisClientConfig);
        RedisConnection redisConnection = redisClient.connect();

        redisConnection.sync(StringCodec.INSTANCE, RedisCommands.SET, "name", "tiger");

        redisConnection.closeAsync();
        redisClient.shutdown();
    }

    static void testLock() {
        String orderId = "1000";

        // 并发重复校验
        RLock rLock = redissonClient.getLock("lock:order:" + orderId);

        try {
            boolean locked = rLock.tryLock(10, 30, TimeUnit.SECONDS);
            if (locked) {
                // TODO 获取到锁，执行业务处理

                try {
                    RBucket<Integer> payStatus = redissonClient.getBucket("order:payStatus:" + orderId);
                    if (payStatus.isExists()) {
                        System.out.println("订单 " + orderId + " 在短时间内重复支付，已拦截");
                        return;
                    }
                    payStatus.set(1, 60, TimeUnit.SECONDS);
                } finally {
                    rLock.unlock(); // 如果持有的时间超过 leaseTime 再 unlock，则会抛异常
                }
            } else {
                System.out.println("订单 " + orderId + " 在短时间内重复支付，已拦截");
                return;
            }
        } catch (InterruptedException e) {
            System.out.println("订单 " + orderId + " 重复校验出现异常:" + e.getMessage());
        }
    }

    static void testRedLock() {
        String orderId = "1000";

        // 并发重复校验
        RLock rLock = redissonClient.getLock("lock:order:" + orderId);
        RedissonRedLock redLock = new RedissonRedLock(rLock);

        try {
            boolean locked = redLock.tryLock(10, 30, TimeUnit.SECONDS);
            if (locked) {
                // TODO 获取到锁，执行业务处理

                try {
                    RBucket<Integer> payStatus = redissonClient.getBucket("order:payStatus:" + orderId);
                    if (payStatus.isExists()) {
                        System.out.println("订单 " + orderId + " 在短时间内重复支付，已拦截");
                        return;
                    }
                    payStatus.set(1, 60, TimeUnit.SECONDS);
                } finally {
                    redLock.unlock(); // 如果持有的时间超过 leaseTime 再 unlock，不会抛异常
                }
            } else {
                System.out.println("订单 " + orderId + " 在短时间内重复支付，已拦截");
                return;
            }
        } catch (InterruptedException e) {
            System.out.println("订单 " + orderId + " 重复校验出现异常:" + e.getMessage());
        }
    }

}
