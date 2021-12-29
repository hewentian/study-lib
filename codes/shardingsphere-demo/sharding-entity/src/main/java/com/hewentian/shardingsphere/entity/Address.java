package com.hewentian.shardingsphere.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Address implements Serializable {

    private static final long serialVersionUID = 3374667432604186392L;

    private Long addressId;

    private String addressName;

}
