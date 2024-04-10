package com.ipoca.bbrpc.demo.api;

import java.util.List;
import java.util.Map;

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

    List<User> getList(List<User> users);

    Map<String, User> getMap(Map<String, User> map);

    Boolean getFlag(boolean b);

    User[] findUsers(User[] users);

    User findById(long l);
}
