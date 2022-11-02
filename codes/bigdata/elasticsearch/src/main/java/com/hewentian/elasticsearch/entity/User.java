package com.hewentian.elasticsearch.entity;

import lombok.*;

import java.util.Date;

/**
 * <p>
 * <b>User</b> 是 测试es用的bean
 * </p>
 *
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User implements Cloneable {
    private Long id;
    private String name;
    private Integer age;
    private String[] tags;
    private Date birthday;

}
