package com.hewentian.shardingsphere.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 263434701950670170L;

    private long orderItemId;

    private long orderId;

    private int userId;

    private String status;
}
