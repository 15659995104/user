package com.example.user.service.impl;

import com.example.user.dao.UserDao;
import com.example.user.dto.AddUser;
import com.example.user.entiy.User;
import com.example.user.service.UserService;
import com.example.user.utils.FilesUtils;
import com.example.user.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public String addUser(AddUser addUser) {
        if (StringUtils.isEmpty(addUser.getUserName())){
            return "添加用户失败，用户名不可为空！";
        }
        if (StringUtils.isEmpty(addUser.getPassWord())){
            return "添加用户失败，用户密码不可为空！";
        }
        if (StringUtils.isEmpty(addUser.getImageUrl())){
            return "添加用户失败，用户头像不可为空！";
        }
        try {
            Map<String, String> map = FilesUtils.saveImgByBase64(addUser.getImageUrl(),addUser.getUserName());
            String p = MD5.MD5Encode(addUser.getPassWord());
            addUser.setPassWord(p);
            for (String key : map.keySet()) {
                if (map.get(key).equals("error")) {
                    return "图片上传失败！";
                }
            }
            String url = map.get("code");
            int index=url.indexOf("/static/images");
            String imageurl = url.substring(index);
            addUser.setImageUrl(imageurl);
            int i = userDao.addUser(addUser);
            if (i!=0){
                return  "SUCCESS";
            }else{
                return "添加失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  e.getMessage();
        }
    }

    @Override
    public User getByUserName(String userName) {
        List<User> userList = userDao.getByUserName(userName);
        if (userList!=null&&userList.size()>0){
            return userList.get(0);
        }
        return null;
    }

    @Override
    public User getById(Integer id) {
        return userDao.getById(id);
    }

    @Override
    public Integer update(User user) {
        return userDao.update(user);
    }
}
