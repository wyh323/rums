package com.happy_hao.rums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy_hao.rums.common.Result;
import com.happy_hao.rums.dto.*;
import com.happy_hao.rums.po.User;
import jakarta.validation.Valid;

public interface UserService extends IService<User> {

    Result registerForm(RegisterRequest registerRequest);

    Result loginForm(LoginRequest loginRequest);

    Result loginFeishu(String code);

    Result userInfo();
}
