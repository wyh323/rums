package com.happy_hao.rums.controller;

import com.happy_hao.rums.common.Result;
import com.happy_hao.rums.dto.*;
import com.happy_hao.rums.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register/up")
    public Result registerUp(@Valid @RequestBody RegisterUpRequest registerUpRequest) {

        return userService.registerUp(registerUpRequest);
    }

    @PostMapping("/register/ep")
    public Result registerEp(@Valid @RequestBody RegisterEpRequest registerEpRequest) {

        return userService.registerEp(registerEpRequest);
    }

    @PostMapping("/register/phone")
    public Result registerPh(@Valid @RequestBody RegisterPhRequest registerPhRequest) {

        return userService.registerPh(registerPhRequest);
    }

    @PostMapping("/login/form")
    public Result login(@Valid @RequestBody LoginRequest loginRequest) {

        return userService.login(loginRequest);
    }

}
