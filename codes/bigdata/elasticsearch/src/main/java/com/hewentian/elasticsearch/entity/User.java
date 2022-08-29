package com.hewentian.elasticsearch.entity;

import io.searchbox.annotations.JestId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * <p>
 * <b>User</b> 是 测试es用的bean
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-09-18 4:25:33 PM
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@ToString
public class User implements Cloneable {
    @JestId
    private Long id;
    private String name;
    private Integer age;
    private String[] tags;
    private Date birthday;

}
