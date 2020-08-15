package com.example.user.dao;

import com.example.user.dto.AddUser;
import com.example.user.entiy.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    int addUser(AddUser addUser);

    List<User> getByUserName(String userName);

    User getById(Integer id);

    Integer update(User user);
}
