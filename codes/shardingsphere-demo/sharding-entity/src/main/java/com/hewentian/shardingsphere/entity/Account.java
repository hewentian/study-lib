package com.hewentian.shardingsphere.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Account implements Serializable {

    private static final long serialVersionUID = -5889545274302226912L;

    private long accountId;

    private int userId;

    private String status;

}
