package com.hewentian.shardingsphere.raw;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class Test {

    public static void main(String[] args) throws SQLException {
        // 分库，不分表
//        Configuration configuration = new Configuration();

        // 同库，分表
//        Configuration2 configuration = new Configuration2();

        // 分库，分表
        Configuration3 configuration = new Configuration3();

        DataSource dataSource = configuration.getDataSource();
        Service service = new Service(dataSource);
        service.run();
    }
}
