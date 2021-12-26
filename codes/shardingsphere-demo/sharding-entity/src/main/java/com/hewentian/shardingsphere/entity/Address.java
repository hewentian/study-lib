package com.hewentian.shardingsphere.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Address implements Serializable {

    private static final long serialVersionUID = 661434701950670670L;

    private Long addressId;

    private String addressName;

}
