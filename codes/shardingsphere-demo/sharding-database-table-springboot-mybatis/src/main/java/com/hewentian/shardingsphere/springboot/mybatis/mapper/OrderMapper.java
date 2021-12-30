package com.hewentian.shardingsphere.springboot.mybatis.mapper;

import com.hewentian.shardingsphere.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    void createTableIfNotExists();

    void truncateTable();

    void dropTable();

    void insert(Order order);

    void delete(long orderId);

    List<Order> selectAll();
}
