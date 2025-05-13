package com.rishi.book.manager.service;

import com.rishi.book.manager.model.User;
import com.rishi.book.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createuser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(String username, User user) {
       //TODO
        return null;
    }

    @Override
    public void deleteUser(String username) {

    }
}
