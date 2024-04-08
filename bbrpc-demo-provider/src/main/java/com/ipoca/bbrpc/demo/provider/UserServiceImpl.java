package com.ipoca.bbrpc.demo.provider;

import com.ipoca.bbrpc.core.annotation.BBProvider;
import com.ipoca.bbrpc.demo.api.User;
import com.ipoca.bbrpc.demo.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@BBProvider
public class UserServiceImpl implements UserService {

    @Autowired
    Environment environment;
    @Override
    public User findById(int id) {
        return new User(id, "bb-V1-" + environment.getProperty("server.port") + "_" + System.currentTimeMillis());
    }

    @Override
    public User findById(int id, String name) {
        return new User(id, "bb-" + environment.getProperty("server.port") + "_" + name + "_" + System.currentTimeMillis());
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

    @Override
    public User ex(boolean flag){
        if (flag) throw new RuntimeException("just throw an exception");
        return new User(100, "BB100");
    }


    String timeoutPorts = "8081,8094";

    @Override
    public User find(int timeout) {
        String prot = environment.getProperty("server.port");
        if (Arrays.stream(timeoutPorts.split(",")).anyMatch(prot::equals)) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return new User(100, "BB100_" + prot);
    }


    public void setTimeoutPorts(String timeoutPorts) {
        this.timeoutPorts = timeoutPorts;
    }
}
