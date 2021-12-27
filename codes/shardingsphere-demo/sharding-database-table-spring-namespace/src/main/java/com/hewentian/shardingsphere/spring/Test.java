package com.hewentian.shardingsphere.spring;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

public class Test {

    public static void main(String[] args) throws SQLException {
        // 分库，不分表
//        String configLocation = "application.xml";

        // 同库，分表
//        String configLocation = "application2.xml";

        // 分库，分表
        String configLocation = "application3.xml";

        try (ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation)) {
            Service service = applicationContext.getBean(Service.class);
            service.run();
        }
    }
}
