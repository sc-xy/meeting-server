package com.example.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.meeting.model.entity.User;
import com.example.meeting.model.vo.LoginUserVO;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *

 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userName 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userName, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userName 用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userName, String userPassword, HttpServletRequest request);
}
