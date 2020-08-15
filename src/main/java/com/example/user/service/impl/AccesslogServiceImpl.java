package com.example.user.service.impl;

import com.example.user.dao.AccesslogDao;
import com.example.user.entiy.Accesslog;
import com.example.user.service.AccesslogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AccesslogServiceImpl implements AccesslogService {
    @Autowired
    private AccesslogDao accesslogDao;
    @Override
    public List<Accesslog> getByUserId(Integer userId) {
        List<Accesslog> accesslogList = accesslogDao.getByUserId(userId);
        return accesslogList;
    }

    @Override
    public void save(Accesslog accesslog) {
        accesslogDao.save(accesslog);
    }
}
