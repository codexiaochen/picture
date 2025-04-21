package com.chen.picture.model.vo;

import com.chen.picture.model.enums.UserRoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserLoginVo implements Serializable {

    private static final long serialVersionUID = -7308777909048949397L;

    /**
     * 用户 id
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
