package org.example.service;

import org.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Return all users.
     */
    List<User> readAllUsers();

    /**
     * Return user details by id.
     */
    Optional<User> readUserById(Long userId);
}
