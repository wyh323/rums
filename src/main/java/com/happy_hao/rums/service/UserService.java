package com.happy_hao.rums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy_hao.rums.common.Result;
import com.happy_hao.rums.dto.LoginRequest;
import com.happy_hao.rums.dto.RegistrationRequest;
import com.happy_hao.rums.po.User;

public interface UserService extends IService<User> {
    Result register(RegistrationRequest registrationRequest);

    Result login(LoginRequest loginRequest);
}
