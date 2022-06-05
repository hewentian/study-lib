package com.hewentian.redisson.service.impl;

import com.hewentian.redisson.entity.User;
import com.hewentian.redisson.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User findById(Integer id) {
        return User.builder().id(1000).name("scott").age(20).build();
    }
}
