package com.example.user.entiy;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Accesslog implements Serializable {
    private Integer id;
    private Integer userId;
    private String userName;
    private String type;
    private Date operateTime;
}
