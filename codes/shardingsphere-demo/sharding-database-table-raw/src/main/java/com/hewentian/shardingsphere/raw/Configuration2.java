package com.hewentian.shardingsphere.raw;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
import org.apache.shardingsphere.mode.repository.standalone.StandalonePersistRepositoryConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class Configuration2 {

    private static final String HOST = "mysql.hewentian.com";

    private static final int PORT = 3306;

    private static final String USER_NAME = "root";

    private static final String PASSWORD = "123456";

    /**
     * Create a DataSource object, which is an object rewritten by ShardingSphere itself
     * and contains various rules for rewriting the original data storage. When in use, you only need to use this object.
     *
     * @return
     * @throws SQLException
     */
    public DataSource getDataSource() throws SQLException {
        return ShardingSphereDataSourceFactory.createDataSource(createModeConfiguration(), createDataSourceMap(), Collections.singleton(createShardingRuleConfiguration()), new Properties());
    }

    private ShardingRuleConfiguration createShardingRuleConfiguration() {
        ShardingRuleConfiguration result = new ShardingRuleConfiguration();
        result.getTables().add(getOrderTableRuleConfiguration());
        result.getTables().add(getOrderItemTableRuleConfiguration());
        result.getBroadcastTables().add("t_address");

        result.getBindingTableGroups().add("t_order");
        result.getBindingTableGroups().add("t_order_item");

        Properties orderProps = new Properties();
        orderProps.setProperty("algorithm-expression", "t_order_$->{order_id % 2}");
        result.getShardingAlgorithms().put("t-order-inline", new ShardingSphereAlgorithmConfiguration("INLINE", orderProps));

        Properties orderItemProps = new Properties();
        orderItemProps.setProperty("algorithm-expression", "t_order_item_$->{order_id % 2}");
        result.getShardingAlgorithms().put("t-order-item-inline", new ShardingSphereAlgorithmConfiguration("INLINE", orderItemProps));

        result.getKeyGenerators().put("snowflake", new ShardingSphereAlgorithmConfiguration("SNOWFLAKE", getProperties()));
        return result;
    }

    private static ModeConfiguration createModeConfiguration() {
        return new ModeConfiguration("Standalone", new StandalonePersistRepositoryConfiguration("File", new Properties()), true);
    }

    private static ShardingTableRuleConfiguration getOrderTableRuleConfiguration() {
        ShardingTableRuleConfiguration result = new ShardingTableRuleConfiguration("t_order", "demo_ds.t_order_${[0,1]}");
        result.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("order_id", "snowflake"));

        result.setTableShardingStrategy(new StandardShardingStrategyConfiguration("order_id", "t-order-inline"));
        return result;
    }

    private static ShardingTableRuleConfiguration getOrderItemTableRuleConfiguration() {
        ShardingTableRuleConfiguration result = new ShardingTableRuleConfiguration("t_order_item", "demo_ds.t_order_item_${[0,1]}");
        result.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("order_item_id", "snowflake"));

        result.setTableShardingStrategy(new StandardShardingStrategyConfiguration("order_id", "t-order-item-inline"));
        return result;
    }

    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>(1, 1);
        result.put("demo_ds", createDataSource("demo_ds"));
        return result;
    }

    private static Properties getProperties() {
        Properties result = new Properties();
        result.setProperty("worker-id", "123");
        return result;
    }

    private DataSource createDataSource(final String dataSourceName) {
        HikariDataSource result = new HikariDataSource();
        result.setDriverClassName("com.mysql.cj.jdbc.Driver");
        result.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8", HOST, PORT, dataSourceName));
        result.setUsername(USER_NAME);
        result.setPassword(PASSWORD);
        return result;
    }
}
