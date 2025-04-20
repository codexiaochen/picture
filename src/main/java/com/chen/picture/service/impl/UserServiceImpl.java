package com.chen.picture.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.picture.model.User;
import com.chen.picture.service.UserService;
import com.chen.picture.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author abc
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-04-20 20:37:54
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




