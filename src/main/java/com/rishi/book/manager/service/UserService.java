package com.rishi.book.manager.service;

import com.rishi.book.manager.model.User;

import java.util.List;

public interface UserService {

    User createuser(User user);

    List<User> findAll();

    User updateUser(String username, User user);

    void deleteUser(String username);
}
