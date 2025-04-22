package com.chen.picture.interceptor;

import com.chen.picture.contant.UserContant;
import com.chen.picture.exception.BusinessException;
import com.chen.picture.exception.ErrorCode;
import com.chen.picture.mapper.UserMapper;
import com.chen.picture.model.entity.User;
import com.chen.picture.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {


    private UserMapper userMapper;
    public LoginInterceptor(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取session
        Object userObj = request.getSession().getAttribute(UserContant.USER_LOGIN_STATE);
        //判断用户信息是否存在
        if(userObj == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //更新用户信息，如果用户更新了信息，可以把最新信息返回。（可选）
        User user = (User) userObj;
        Long userId = user.getId();
        User currentUser = userMapper.selectById(userId);
        //将用户信息放进ThreadLocal
        UserHolder.setUser(currentUser);
        //放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //将用户信息删除,防止内存泄漏
        UserHolder.removeUser();
    }
}