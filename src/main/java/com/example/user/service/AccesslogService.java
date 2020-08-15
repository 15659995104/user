package com.example.user.service;

import com.example.user.entiy.Accesslog;

import java.util.List;

public interface AccesslogService {
    List<Accesslog> getByUserId(Integer userId);

    void save(Accesslog accesslog);
}
