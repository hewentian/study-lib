package com.hewentian.shardingsphere.springboot.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "t_order_item")
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 263434701950670170L;

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderItemId;

    @Column(name = "order_id")
    private long orderId;
    
    @Column(name = "user_id")
    private int userId;

    @Column(name = "status")
    private String status;

}
