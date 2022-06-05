package com.hewentian.redisson.entity;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Builder
public class User {
    private Integer id;

    private String name;

    private Integer age;

    private String gender;

    private String phone;

    private String address;

    private Date birthday;

    private Date createTime;
}
