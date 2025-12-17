package org.example.service;

import org.example.exceptions.GeneralException;
import org.example.model.User;

import java.util.List;
import java.util.Map;


public interface UserService {

    List<User> readAllUsers(Map<Long,User> users) throws GeneralException;

}