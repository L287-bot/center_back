package com.example.center.entity.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -2170040691909011383L;
    private String userAccount;
    private  String userPassword;

}
