package com.hewentian.mongodb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * <b>T</b> 是
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2019-03-06 09:22:14
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = 2742179856443542632L;

    private int id;
    private String password;
    private UserInfo info;
    private List<String> title;
    private String updateTime; // 两种时间格式，它们的查询方式不同
    private Date createTime;

}
