package com.example.user.service;

import com.example.user.dto.AddUser;
import com.example.user.entiy.User;

public interface UserService {

    String addUser(AddUser addUser);

    User getByUserName(String userName);

    User getById(Integer id);

    Integer update(User user);
}
