package com.ipoca.bbrpc.demo.api;

import java.util.List;

public interface UserService {

    User findById(int id);

    User findById(int id, String name);

    long getId(long id);

    long getId(User user);


    String getName();

    String getName(int id);

    int[] getIds();

    List<Integer> getIdList();

    List<User> getUserList();

    long[] getLongIds();

    int[] getIds(int[] ids);

    User ex(boolean flag);

    User find(int timeout);

    void setTimeoutPorts(String timeoutPorts);
}
