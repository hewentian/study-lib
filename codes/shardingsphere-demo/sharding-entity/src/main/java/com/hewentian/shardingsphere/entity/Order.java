package com.hewentian.shardingsphere.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Order implements Serializable {

    private static final long serialVersionUID = 661434701950670670L;

    private long orderId;

    private int userId;

    private long addressId;

    private String status;

}
