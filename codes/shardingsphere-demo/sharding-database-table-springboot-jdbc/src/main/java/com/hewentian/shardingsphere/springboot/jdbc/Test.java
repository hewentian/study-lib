package com.hewentian.shardingsphere.springboot.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.SQLException;

@SpringBootApplication
public class Test {
    public static void main(String[] args) throws SQLException {
        try (ConfigurableApplicationContext applicationContext = SpringApplication.run(Test.class, args)) {
            Service service = applicationContext.getBean(Service.class);
            service.run();
        }
    }
}
