package com.chen.picture.controller;

import com.chen.picture.common.BaseResponse;
import com.chen.picture.common.ResultUtils;
import com.chen.picture.model.dto.UserRegisterRequest;
import com.chen.picture.model.dto.UserLoginRequest;
import com.chen.picture.model.vo.UserLoginVo;
import com.chen.picture.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
