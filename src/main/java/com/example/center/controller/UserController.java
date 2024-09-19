package com.example.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.center.common.BaseResponse;
import com.example.center.common.ErrorCode;
import com.example.center.common.ResultUtils;
import com.example.center.entity.User;
import com.example.center.entity.request.UserLoginRequest;
import com.example.center.entity.request.UserRegisterRequest;
import com.example.center.exception.BusinessException;
import com.example.center.service.UserService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.center.constant.UserConstant.ADMIN_ROLE;
import static com.example.center.constant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户接口
 */
@RestController

@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 公共鉴权代码 校验是否为管理员
     * 只有管理员可以调用search和delete方法
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request)
    {
        //拿到登录状态，仅有管理员可以查询
        Object userObj=request.getSession().getAttribute(USER_LOGIN_STATE);
        User user=(User)userObj;
        System.out.println(user.getUserStatus());
        return user != null && user.getUserStatus() == ADMIN_ROLE;

    }

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest==null)
        {
            return null;
        }
        String userAccount=userRegisterRequest.getUserAccount();
        String userPassword=userRegisterRequest.getUserPassword();
        String checkPassword=userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword))
        {
            return null;
        }
        long result=userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest,  HttpServletRequest request)
    {
        if (userLoginRequest==null)
        {

            return ResultUtils.error(ErrorCode.PARAM_ERROR);
        }
        String userAccount=userLoginRequest.getUserAccount();
        System.out.println(userAccount);
        String userPassword=userLoginRequest.getUserPassword();
        System.out.println(userPassword);
        if(StringUtils.isAnyBlank(userAccount,userPassword))
        {
            return ResultUtils.error(ErrorCode.PARAM_ERROR);
        }
          User user= userService.doLogin(userAccount, userPassword,request);
        return ResultUtils.success(user);
    }

    /**
     * 查询所有用户(管理员)
     * @param username
     * @param request
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String username,HttpServletRequest request){
       if(!isAdmin(request))
       {
           throw new BusinessException(ErrorCode.NO_AUTH,"你没有权限进行次操作");
       }

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(username))
        {
            queryWrapper.like("username",username);
        }
        //查询到所有用户
          List<User> userList=  userService.list(queryWrapper);
        //用户列表中的每一个人依次脱敏
        List<User> list =userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());

        return ResultUtils.success(list);
    }

    /**
     * 删除用户
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser( long id, HttpServletRequest request)
    {
        //拿到登录状态，仅有管理员可以删除用户
        if(!isAdmin(request))
        {
            throw new BusinessException(ErrorCode.NO_AUTH,"你没有权限进行次操作");
        }
        if(id<=0)
        {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        boolean a= userService.removeById(id);
        return ResultUtils.success(a);
    }

    /**
     * 根据id查找用户
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/find")
    public  BaseResponse<User> findById( long id,HttpServletRequest request)
    {
      if(!isAdmin(request))
      {
          throw  new BusinessException(ErrorCode.NO_AUTH,"你没有权限进行此操作");
      }
      if (id<=0)
      {
          throw new BusinessException(ErrorCode.PARAM_ERROR);
      }
      User user=userService.getById(id);
      return ResultUtils.success(userService.getSafetyUser(user));
    }

    /**
     * 用户信息修改
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody User user,HttpServletRequest request)
    {   if (user==null)
    {
        throw new BusinessException(ErrorCode.PARAM_ERROR);
    }
        if(!isAdmin(request))
        {
            throw  new BusinessException(ErrorCode.NO_AUTH,"你没有权限进行此操作");
        }
        return ResultUtils.success(userService.updateById(user));
    }





    /**
     * 得到当前用户信息
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request)
    {
        Object uerObj=request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser=(User) uerObj;
        if (currentUser==null)
        {
            throw new  BusinessException(ErrorCode.PARAM_ERROR);
        }
        long userId=currentUser.getId();
        User user=userService.getById(userId);
        return ResultUtils.success(userService.getSafetyUser(user));
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public  BaseResponse<Integer> userLogout (HttpServletRequest request)
    {
        if (request==null)
        {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        return ResultUtils.success(userService.userLogOut(request));
    }







}
