package com.hewentian.shardingsphere.springboot.mybatis.mapper;

import com.hewentian.shardingsphere.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    void createTableIfNotExists();

    void truncateTable();

    void dropTable();

    void insert(OrderItem orderItem);

    void delete(long orderId);

    List<OrderItem> selectAll();
}
