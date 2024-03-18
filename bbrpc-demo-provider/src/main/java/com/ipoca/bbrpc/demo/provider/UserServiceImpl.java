package com.ipoca.bbrpc.demo.provider;

import com.ipoca.bbrpc.core.annotation.BBProvider;
import com.ipoca.bbrpc.demo.api.User;
import com.ipoca.bbrpc.demo.api.UserService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@BBProvider
public class UserServiceImpl implements UserService {


    @Override
    public User findById(int id) {
        return new User(id, "bb-" + System.currentTimeMillis());
    }

    @Override
    public User findById(int id, String name) {
        return new User(id, "bb-" + name + "_" + System.currentTimeMillis());
    }

    @Override
    public long getId(long id) {
        return id;
    }

    @Override
    public long getId(User user) {
        return user.getId().longValue();
    }

    @Override
    public String getName() {
        return "bb";
    }

    @Override
    public String getName(int id) {
        return "bb-" + id;
    }

    @Override
    public int[] getIds() {
        return new int[] {1,2,3};
    }

    @Override
    public List<Integer> getIdList() {
        return Arrays.asList(1,2,3);
    }

    @Override
    public List<User> getUserList() {
        return Arrays.asList(new User(1,"x"), new User(2, "xx"));
    }

    @Override
    public long[] getLongIds() {
        return new long[]{100,200,300};
    }

    @Override
    public int[] getIds(int[] ids) {
        return ids;
    }


}
