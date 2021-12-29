package com.hewentian.shardingsphere.springboot.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

@EntityScan(basePackages = "com.hewentian.shardingsphere.springboot.jpa.entity")
@SpringBootApplication
public class Test {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext applicationContext = SpringApplication.run(Test.class, args)) {
            Service service = applicationContext.getBean(Service.class);
            service.run();
        }
    }
}
