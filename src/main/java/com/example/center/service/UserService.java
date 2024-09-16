package com.example.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.center.entity.User;
import jakarta.servlet.http.HttpServletRequest;



import java.net.http.HttpRequest;


/**
* @author ASUS
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-09-13 00:06:13
*/
public interface UserService extends IService<User> {


    /**|
     *用户注册
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 再次输入密码
     * @return
     */
    long userRegister (String userAccount,String userPassword,String checkPassword);

    /**
     * 返回脱敏用户信息
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户登录
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param request
     * @return 用户信息
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogOut(HttpServletRequest request);

}
