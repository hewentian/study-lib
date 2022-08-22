package com.hewentian.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * <b>T</b> 是
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2019-03-06 09:21:13
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -971460881384487765L;

    private String name;

    /**
     * 1.男；2.女
     */
    private int sex;

}
