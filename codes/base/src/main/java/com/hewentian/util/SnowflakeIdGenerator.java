package com.hewentian.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>
 * <b>SnowflakeIdGenerator</b> 是 Snowflake 基于雪花算法的ID生成器
 * </p>
 */
public class SnowflakeIdGenerator {
    /**
     * ID中41位时间戳的起点 (2020-01-01 00:00:00.00)
     * <p>
     * 一般地，选用系统上线的时间
     */
    private final long startPoint = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli();

    /**
     * 序列号位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID位数
     */
    private final long workerIdBits = 5L;

    /**
     * 数据中心ID位数
     */
    private final long dataCenterIdBits = 5L;

    /**
     * 序列号最大值, 4095
     *
     * @apiNote 4095 = 0xFFF,其相当于是序列号掩码
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 机器ID最大值, 31
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 数据中心ID最大值, 31
     */
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /**
     * 机器ID左移位数, 12
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据中心ID左移位数, 12+5
     */
    private final long dataCenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间戳左移位数, 12+5+5
     */
    private final long timestampShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * 数据中心ID, Value Range: [0,31]
     */
    private final long dataCenterId;

    /**
     * 机器ID, Value Range: [0,31]
     */
    private final long workerId;

    /**
     * 相同毫秒内的序列号, Value Range: [0,4095]
     */
    private long sequence = 0L;

    /**
     * 上一个生成ID的时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 构造器
     *
     * @param dataCenterId 数据中心ID
     * @param workerId     机器中心ID
     */
    public SnowflakeIdGenerator(Long dataCenterId, Long workerId) {
        if (dataCenterId == null || dataCenterId < 0 || dataCenterId > maxDataCenterId
                || workerId == null || workerId < 0 || workerId > maxWorkerId) {
            throw new IllegalArgumentException("输入参数错误");
        }

        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    /**
     * 获取ID
     */
    public synchronized long nextId() {
        long currentTimeMillis = System.currentTimeMillis();

        // 当前时间小于上一次生成ID的时间戳，系统时钟被回拨
        if (currentTimeMillis < lastTimestamp) {
//            return handleTimeMovedBackwards(currentTimeMillis);
            throw new RuntimeException("系统时钟被回拨");
        }

        // 当前时间等于上一次生成ID的时间戳，则通过序列号来区分
        if (currentTimeMillis == lastTimestamp) {
            // 通过序列号掩码实现只取 (sequence+1) 的低12位结果，其余位全部清零
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) { // 该时间戳下的序列号已经溢出
                // 阻塞等待下一个毫秒，并获取新的时间戳
                currentTimeMillis = getNextMs(lastTimestamp);
            }
        } else {
            // 当前时间大于上一次生成ID的时间戳，重置序列号
            sequence = 0;
        }

        // 更新上次时间戳信息
        lastTimestamp = currentTimeMillis;

        // 生成此次ID
        long nextId = ((currentTimeMillis - startPoint) << timestampShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;

        return nextId;
    }

    /**
     * 阻塞等待，直到获取新的时间戳(下一个毫秒)
     *
     * @param lastTimestamp 上一个生成ID的时间戳
     * @return 新的时间戳(下一个毫秒)
     */
    private long getNextMs(long lastTimestamp) {
        long currentTimeMillis = System.currentTimeMillis();
        while (currentTimeMillis <= lastTimestamp) {
            currentTimeMillis = System.currentTimeMillis();
        }

        return currentTimeMillis;
    }

    //  下面为处理时钟回拔的可能方案 --------------------------------------------------------------------------------------
    /**
     * 步长，1024
     */
    private static long stepSize = 1024;

    /**
     * 基础序列号, 每发生一次时钟回拨, basicSequence += stepSize
     */
    private long basicSequence = 0L;

    private long handleTimeMovedBackwards(long currentTimeMillis) {
        basicSequence += stepSize;
        if (basicSequence == sequenceMask + 1) {
            basicSequence = 0;
            currentTimeMillis = getNextMs(lastTimestamp);
        }

        sequence = basicSequence;

        // 更新上次时间戳信息
        lastTimestamp = currentTimeMillis;

        // 生成此次ID
        long nextId = ((currentTimeMillis - startPoint) << timestampShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;

        return nextId;
    }

}
