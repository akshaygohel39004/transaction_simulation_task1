package org.example.service;

import org.example.exceptions.GeneralException;
import org.example.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryUserService implements UserService {

    @Override
    public List<User> readAllUsers(Map<Long,User> users) throws GeneralException {
        if(users.isEmpty()){
            throw new GeneralException("No users found");
        }
        return new ArrayList<>(users.values());
    }
}
