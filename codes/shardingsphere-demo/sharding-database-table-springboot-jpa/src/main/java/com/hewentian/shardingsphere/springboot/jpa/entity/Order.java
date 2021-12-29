package com.hewentian.shardingsphere.springboot.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "t_order")
public class Order implements Serializable {
    private static final long serialVersionUID = 661434701950670670L;

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "address_id")
    private long addressId;

    @Column(name = "status")
    private String status;

}
