package com.ipoca.bbrpc.demo.api;

public interface UserService {

    User findById(int id);

    int getId(int id);

    String getName();
}
