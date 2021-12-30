package com.hewentian.shardingsphere.springboot.mybatis;

import com.hewentian.shardingsphere.entity.Address;
import com.hewentian.shardingsphere.entity.Order;
import com.hewentian.shardingsphere.entity.OrderItem;
import com.hewentian.shardingsphere.springboot.mybatis.mapper.AddressMapper;
import com.hewentian.shardingsphere.springboot.mybatis.mapper.OrderItemMapper;
import com.hewentian.shardingsphere.springboot.mybatis.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private AddressMapper addressMapper;

    /**
     * Execute test.
     */
    public void run() {
        try {
            this.initEnvironment();
            this.processSuccess();
        } finally {
//            this.cleanEnvironment();
        }
    }

    /**
     * Initialize the database test environment.
     */
    private void initEnvironment() {
        orderMapper.createTableIfNotExists();
        orderItemMapper.createTableIfNotExists();
        addressMapper.createTableIfNotExists();
        orderMapper.truncateTable();
        orderItemMapper.truncateTable();
        addressMapper.truncateTable();

        initAddressData();
    }

    private void processSuccess() {
        System.out.println("-------------- Process Success Begin ---------------");
        List<Long> orderIds = insertData();
        printData();
//        deleteData(orderIds);
//        printData();
        System.out.println("-------------- Process Success Finish --------------");
    }

    private void processFailure() {
        System.out.println("-------------- Process Failure Begin ---------------");
        insertData();
        System.out.println("-------------- Process Failure Finish --------------");
        throw new RuntimeException("Exception occur for transaction test.");
    }

    private List<Long> insertData() {
        System.out.println("---------------------------- Insert Data ----------------------------");
        List<Long> result = new ArrayList<>(10);
        for (int i = 1; i <= 10; i++) {
            Order order = insertOrder(i);
            result.add(order.getOrderId());
        }

        for (int i = 1; i <= 10; i++) {
            insertOrderItem(i, result.get(i - 1));
        }

        return result;
    }

    private Order insertOrder(final int i) {
        Order order = new Order();
        order.setUserId(i);
        order.setAddressId(i);
        order.setStatus("INSERT_TEST");
        orderMapper.insert(order);
        return order;
    }

    private void insertOrderItem(final int userId, final long orderId) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        orderItem.setUserId(userId);
        orderItem.setStatus("INSERT_TEST");
        orderItemMapper.insert(orderItem);
    }

    private void deleteData(final List<Long> orderIds) {
        System.out.println("---------------------------- Delete Data ----------------------------");
        for (Long each : orderIds) {
            orderMapper.delete(each);
            orderItemMapper.delete(each);
        }
    }

    private void printData() {
        System.out.println("---------------------------- Print Order Data -----------------------");
        for (Object each : orderMapper.selectAll()) {
            System.out.println(each);
        }
        System.out.println("---------------------------- Print OrderItem Data -------------------");
        for (Object each : orderItemMapper.selectAll()) {
            System.out.println(each);
        }
    }

    private void initAddressData() {
        for (int i = 0; i < 10; i++) {
            Address address = new Address();
            address.setAddressId((long) i);
            address.setAddressName("address_" + i);
            addressMapper.insert(address);
        }
    }

    /**
     * Restore the environment.
     */
    private void cleanEnvironment() {
        orderMapper.dropTable();
        orderItemMapper.dropTable();
    }
}
