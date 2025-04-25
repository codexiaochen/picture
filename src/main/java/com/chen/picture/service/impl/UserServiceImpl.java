package com.chen.picture.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.picture.exception.BusinessException;
import com.chen.picture.exception.ErrorCode;
import com.chen.picture.exception.ThrowUtils;
import com.chen.picture.model.dto.user.UserQueryRequest;
import com.chen.picture.model.entity.User;
import com.chen.picture.model.enums.UserRoleEnum;
import com.chen.picture.model.dto.user.UserRegisterRequest;
import com.chen.picture.model.vo.UserLoginVo;
import com.chen.picture.model.vo.UserVO;
import com.chen.picture.service.UserService;
import com.chen.picture.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.chen.picture.contant.UserContant.USER_LOGIN_STATE;

/**
* @author abc
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-04-20 20:37:54
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

//    @Resource
//    private StringRedisTemplate stringRedisTemplate;

    /**
     * 注册功能
     * @param userRegisterRequest
     * @return userId
     */
    @Override
    public void userRegister(UserRegisterRequest userRegisterRequest) {
        //1、校验参数
        if (StrUtil.isBlank(userRegisterRequest.getUserAccount())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名为空");
        }
        if (StrUtil.isBlank(userRegisterRequest.getUserPassword())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码为空");
        }
        if (StrUtil.isBlank(userRegisterRequest.getCheckPassword())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"确认密码为空");
        }
        if(userRegisterRequest.getUserPassword().length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度太短，应该大于8");
        }
        if(!userRegisterRequest.getUserPassword().equals(userRegisterRequest.getCheckPassword())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }

        //2、检查数据库中是否存在该用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userRegisterRequest.getUserAccount());
        Long count = userMapper.selectCount(wrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户信息已经存在");
        }

        //3、密码加密+加盐
        String password = getencrypt(userRegisterRequest.getUserPassword());
        //4、将新用户信息加入到数据库中
        User user = new User();
        user.setUserAccount(userRegisterRequest.getUserAccount());
        user.setUserPassword(password);
        String name_suffix = UUID.randomUUID().toString().replace("-","");
        user.setUserName("user_" + name_suffix);
        user.setUserRole(UserRoleEnum.USER);
        boolean save = save(user);
        if(!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"系统错误，注册失败");
        }

  }

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return userLoginVo
     */
    @Override
    public UserLoginVo userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        //1、校验参数
        ThrowUtils.throwIf(StrUtil.isBlank(userAccount),ErrorCode.PARAMS_ERROR,"用户名不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(userPassword),ErrorCode.PARAMS_ERROR,"密码不能为空");
        //2、检查是否注册 密码是否正确
        String password = getencrypt(userPassword);
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("userAccount", userAccount)
                .eq("userPassword", password);
        User user = userMapper.selectOne(queryWrapper);
        ThrowUtils.throwIf(user == null,ErrorCode.PARAMS_ERROR,"用户不存在或密码错误");

        //3、返回信息给前端
        UserLoginVo userLoginVo = new UserLoginVo();
        BeanUtil.copyProperties(user,userLoginVo);
        userLoginVo.setUserRole(String.valueOf(user.getUserRole()).toLowerCase());
        request.getSession().setAttribute(USER_LOGIN_STATE,user);

        return userLoginVo;
    }

    /**
     * 密码加密算法
     * @param password
     * @return
     */
    @Override
    public String getencrypt(String password){
        final String SALT = "picture";
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes());
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接返回上述结果）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 用户退出登录
     *
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }


    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjUtil.isNotNull(id), "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }



}