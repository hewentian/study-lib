package com.hewentian.shardingsphere.springboot.jpa;

import com.hewentian.shardingsphere.springboot.jpa.entity.Address;
import com.hewentian.shardingsphere.springboot.jpa.entity.Order;
import com.hewentian.shardingsphere.springboot.jpa.entity.OrderItem;
import com.hewentian.shardingsphere.springboot.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private JpaRepository repository;

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
            Order order = new Order();
            order.setUserId(i);
            order.setAddressId(i);
            order.setStatus("INSERT_TEST");

            repository.insertOrder(order);
            result.add(order.getOrderId());
        }

        for (int i = 1; i <= 10; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(result.get(i - 1));
            orderItem.setUserId(i);
            orderItem.setStatus("INSERT_TEST");
            repository.insertOrderItem(orderItem);
        }

        return result;
    }

    private void deleteData(final List<Long> orderIds) {
        System.out.println("---------------------------- Delete Data ----------------------------");
        for (Long each : orderIds) {
            repository.deleteOrder(each);
            repository.deleteOrderItem(each);
        }
    }

    private void printData() {
        System.out.println("---------------------------- Print Order Data -----------------------");
        for (Object each : repository.selectAllOrder()) {
            System.out.println(each);
        }
        System.out.println("---------------------------- Print OrderItem Data -------------------");
        for (Object each : repository.selectAllOrderItem()) {
            System.out.println(each);
        }
    }

    private void initAddressData() {
        for (int i = 0; i < 10; i++) {
            Address address = new Address();
            address.setAddressId((long) i);
            address.setAddressName("address_" + i);

            repository.insertAddress(address);
        }
    }

}
