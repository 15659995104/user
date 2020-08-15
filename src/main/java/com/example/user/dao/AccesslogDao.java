package com.example.user.dao;

import com.example.user.entiy.Accesslog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccesslogDao {
    List<Accesslog> getByUserId(Integer userId);

    void save(Accesslog accesslog);
}
