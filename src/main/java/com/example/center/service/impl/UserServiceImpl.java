package com.example.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.center.common.ErrorCode;
import com.example.center.exception.BusinessException;
import com.example.center.mapper.UserMapper;
import com.example.center.service.UserService;
import com.example.center.entity.User ;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.center.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author ASUS
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-09-13 00:06:13
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private  UserMapper userMapper;

        /**
         * 盐值
         */
        private static final String  SALT="lsc";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //校验传入非空
      if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword))
      {
          throw new BusinessException(ErrorCode.PARAM_ERROR, "参数为空");
      }
      //账号长度
      if (userAccount.length()<4)
      {
          throw new BusinessException(ErrorCode.PARAM_ERROR, "账号过短");
      }
      //密码校验码长度
      if(userPassword.length()<8||checkPassword.length()<8)
      {
          throw new BusinessException(ErrorCode.PARAM_ERROR, "密码过短");

      }
      //账号不能存在非法字符
        String validRule = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%…… &*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher= Pattern.compile(validRule).matcher(userAccount);
        if (matcher.find())
        {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户账号存在非法字符");
        }
        //密码和校验码相同
        if(!userPassword.equals(checkPassword))
        {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "两次输入密码不一致");
        }
        //账号不能重复
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        Long count=userMapper.selectCount(queryWrapper);
        if (count>0)
        {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "账号重复");
        }


        //用户密码加密存储在数据库中
        String encryptPassword= DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        User user=new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean result=this.save(user);
        if (!result)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误，账号注册失败");
        }
        return user.getId();

    }

    @Override
    /**用户脱敏通用方法
     * @Param originUser
     */
    public User getSafetyUser(User originUser)
    {
        //用户脱敏
        User safeUser=new User();
        safeUser.setId(originUser.getId());
        safeUser.setUsername(originUser.getUsername());
        safeUser.setAvatarUrl(originUser.getAvatarUrl());
        safeUser.setGender(originUser.getGender());
        safeUser.setUserAccount(originUser.getUserAccount());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setCreatTime(originUser.getCreatTime());
        safeUser.setUserStatus(originUser.getUserStatus());
        return safeUser;
    }
    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //校验传入非空
        if(StringUtils.isAnyBlank(userAccount,userPassword))
        {
            return null;
        }
        //账号长度
        if (userAccount.length()<4)
        {
            return null;
        }
        //密码校验码长度
        if(userPassword.length()<8)
        {
            return null;

        }
        //账号不能存在非法字符
        String validRule = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%…… &*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher= Pattern.compile(validRule).matcher(userAccount);
        if (matcher.find())
        {
            return null;
        }
        //将传入密码加密与数据库中存储的密码进行比较
        String encryptPassword= DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user=userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user==null)
        {
            log.info("login failed,userAccount can not match userPassword");
            return null;
        }
          //用户脱敏
          User safetyUser=getSafetyUser(user);

        //记录用户登录状态,后续要进行删除查询用户，要进行鉴权，拿到登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        //返回脱敏用户信息
        return safetyUser;
    }

    @Override
    public int userLogOut(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




