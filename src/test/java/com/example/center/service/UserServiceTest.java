package com.example.center.service;
import java.util.Date;
import com.example.center.mapper.UserMapper;

import com.example.center.entity.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
/**
 * 跑通Mybatis plus 自动生成的代码功能
 */
public class UserServiceTest {
    @Resource
    private UserService userService;
    @Test
    public void testAdduser(){
        User user=new User();

        user.setUsername("小强一号");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserAccount("2094676344");
        user.setUserPassword("123456");
        user.setPhone("23123");
        user.setEmail("hhh@qq.com");
        user.setCreatTime(new Date());
        user.setUpdateTime(new Date());
       boolean result= userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);


    }


    @Test
    void userRegister() {
        String userAccount="李大爷";
        String userPassword="";
        String checkPassword="123456";
        Long result=userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount="李大妈";
        result=userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount="李大爷爷";
        userPassword="123456";
        result=userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount="李 夜夜";
        userPassword="12345678";
        result=userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount="正确账号1";
        userPassword="12345678";
        checkPassword="12345678";
        result=userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertTrue(result>0);
    }
}