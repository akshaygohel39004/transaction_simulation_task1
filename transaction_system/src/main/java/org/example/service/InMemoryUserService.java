package org.example.service;

import org.example.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple thread-safe in-memory implementation of UserService for view-only feature.
 * This keeps all users in a ConcurrentHashMap keyed by userId.
 */
public class InMemoryUserService implements UserService {

    private final Map<Long, User> users = new ConcurrentHashMap<>();

    public InMemoryUserService() {
        // optional: seed sample data
        seedSampleData();
    }

    @Override
    public List<User> readAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> readUserById(Long userId) {
        if (userId == null) return Optional.empty();
        return Optional.ofNullable(users.get(userId));
    }

    /* --------- helper methods for tests / wiring --------- */

    public void addUser(User user) {
        if (user == null || user.getUserId() == null) {
            throw new IllegalArgumentException("user and userId must not be null");
        }
        users.put(user.getUserId(), user);
    }

    public void removeUser(Long userId) {
        users.remove(userId);
    }

    private void seedSampleData() {
        // lightweight sample users (IDs chosen arbitrarily)
        User u1 = new User(1L, "Alice", "alice@example.com", "9999999991");
        User u2 = new User(2L, "Bob", "bob@example.com", "9999999992");
        addUser(u1);
        addUser(u2);
    }
}
