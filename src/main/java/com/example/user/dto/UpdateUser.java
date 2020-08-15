package com.example.user.dto;

import lombok.Data;

@Data
public class UpdateUser {
    private String userId;
    private String passWord;
    private String imageUrl;
}
