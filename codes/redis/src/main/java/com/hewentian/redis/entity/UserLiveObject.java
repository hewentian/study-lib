package com.hewentian.redis.entity;

import lombok.*;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

@REntity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Builder
public class UserLiveObject {
    @RId
    private Integer id;

    private String name;

    private Integer age;
}
