package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);

    User findById(Long userId);

    Long findIdByUsername(String username);

    boolean create(String username, String password);
}
