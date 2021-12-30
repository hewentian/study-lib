package com.hewentian.shardingsphere.springboot.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.SQLException;

@MapperScan("com.hewentian.shardingsphere.springboot.mybatis.mapper")
@SpringBootApplication
public class Test {
    public static void main(String[] args) throws SQLException {
        try (ConfigurableApplicationContext applicationContext = SpringApplication.run(Test.class, args)) {
            Service service = applicationContext.getBean(Service.class);
            service.run();
        }
    }
}
