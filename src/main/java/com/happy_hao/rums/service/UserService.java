package com.happy_hao.rums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy_hao.rums.common.Result;
import com.happy_hao.rums.dto.*;
import com.happy_hao.rums.po.User;

public interface UserService extends IService<User> {

    Result registerUp(RegisterUpRequest registerUpRequest);

    Result registerEp(RegisterEpRequest registerEpRequest);

    Result registerPh(RegisterPhRequest registerPhRequest);

    Result registerFs(String code);

    Result loginForm(LoginRequest loginRequest);

    Result loginFeishu(String code);

    Result userInfo();
}
