package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.pojo;

import lombok.Data;

@Data
public class AdminUser {
    private Integer id;
    private String password;
    private String email;
    private String realName;
    private Integer status;

}
