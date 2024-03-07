package com.ipoca.bbrpc.demo.provider;

import com.ipoca.bbrpc.core.annotation.BBProvider;
import com.ipoca.bbrpc.demo.api.User;
import com.ipoca.bbrpc.demo.api.UserService;
import org.springframework.stereotype.Component;

@Component
@BBProvider
public class UserServiceImpl implements UserService {


    @Override
    public User findById(int id) {
        return new User(id, "bb-" + System.currentTimeMillis());
    }

}
