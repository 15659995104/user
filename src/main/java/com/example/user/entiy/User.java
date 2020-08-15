package com.example.user.entiy;

import lombok.Data;

import java.io.Serializable;
@Data
public class User implements Serializable {
    Integer id;
    String userName;
    String passWord;
    String imageUrl;
}
