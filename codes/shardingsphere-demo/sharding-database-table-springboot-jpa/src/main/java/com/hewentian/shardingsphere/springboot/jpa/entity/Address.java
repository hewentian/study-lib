package com.hewentian.shardingsphere.springboot.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "t_address")
public class Address implements Serializable {
    private static final long serialVersionUID = 3374667432604186392L;

    @Id
    @Column(name = "address_id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long addressId;

    @Column(name = "address_name")
    private String addressName;

}
