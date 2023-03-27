package com.hewentian.util;

/**
 * <p>
 * <b>SnowflakeIdGeneratorTest</b> 是
 * </p>
 */
public class SnowflakeIdGeneratorTest {
    public static void main(String[] args) {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1L, 2L);
        System.out.println(snowflakeIdGenerator.nextId());
    }
}
