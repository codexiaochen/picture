package com.chen.picture.controller;

import cn.hutool.core.bean.BeanUtil;
import com.chen.picture.annotation.AuthCheck;
import com.chen.picture.common.BaseResponse;
import com.chen.picture.common.ResultUtils;
import com.chen.picture.contant.UserContant;
import com.chen.picture.exception.ErrorCode;
import com.chen.picture.exception.ThrowUtils;
import com.chen.picture.model.dto.UserRegisterRequest;
import com.chen.picture.model.dto.UserLoginRequest;
import com.chen.picture.model.entity.User;
import com.chen.picture.model.enums.UserRoleEnum;
import com.chen.picture.model.vo.UserLoginVo;
import com.chen.picture.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController   //返回JSON数据
@RequestMapping("/user")
public class UserController {

    @Resource  //@Autowired默认按byType自动装配，而@Resource默认byName自动装配。@Autowired默认先按byType进行匹配，如果发现找到多个bean，则又按照byName方式进行匹配，如果还有多个，则报出异常。@Autowired如果要使用byName，需要使用@Qualifier一起配合。
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<String> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {//@RequestBody用来接收前端传递给后端的json字符串中的数据的
        userService.userRegister(userRegisterRequest);
        return ResultUtils.success("注册成功");
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginVo> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {//@RequestBody用来接收前端传递给后端的json字符串中的数据的
        UserLoginVo userLoginVo = userService.userLogin(userLoginRequest.getUserAccount(), userLoginRequest.getUserPassword(), httpServletRequest);
        return ResultUtils.success(userLoginVo);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<UserLoginVo> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        UserLoginVo userLoginVo = new UserLoginVo();
        BeanUtil.copyProperties(loginUser, userLoginVo);
        userLoginVo.setUserRole(String.valueOf(loginUser.getUserRole()).toLowerCase());
        return ResultUtils.success(userLoginVo);
    }

    /**
     * 用户退出登录
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }


}
