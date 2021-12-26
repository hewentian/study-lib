package com.hewentian.shardingsphere.raw;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class MemoryLocalShardingJdbcExample {

    public static void main(String[] args) throws SQLException {
        // 分库，不分表
//        MemoryLocalShardingJdbcConfiguration shardingConfiguration = new MemoryLocalShardingJdbcConfiguration();

        // 同库，分表
//        MemoryLocalShardingJdbcConfiguration2 shardingConfiguration = new MemoryLocalShardingJdbcConfiguration2();

        // 分库，分表
        MemoryLocalShardingJdbcConfiguration3 shardingConfiguration = new MemoryLocalShardingJdbcConfiguration3();

        DataSource dataSource = shardingConfiguration.getDataSource();
        MemoryLocalShardingJdbcExampleService exampleService = new MemoryLocalShardingJdbcExampleService(dataSource);
        exampleService.run();
    }
}
