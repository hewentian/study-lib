package com.hewentian.redis.service.impl;

import com.hewentian.redis.entity.User;
import com.hewentian.redis.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User findById(Integer id) {
        return User.builder().id(1000).name("scott").age(20).build();
    }
}
