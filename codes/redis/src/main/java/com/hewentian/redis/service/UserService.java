package com.hewentian.redis.service;

import com.hewentian.redis.entity.User;

public interface UserService {
    User findById(Integer id);
}
