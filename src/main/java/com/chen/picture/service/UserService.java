package com.chen.picture.service;

import com.chen.picture.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.picture.model.dto.UserRegisterRequest;
import com.chen.picture.model.vo.UserLoginVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author abc
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-04-20 20:37:54
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    void userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @param userAccount
     * @param password
     * @param request
     * @return userLoginVo
     */
    UserLoginVo userLogin(String userAccount, String password, HttpServletRequest request);

    /**
     * 密码加密算法
     * @param password
     * @return
     */
    String getencrypt(String password);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户退出登录
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

}
