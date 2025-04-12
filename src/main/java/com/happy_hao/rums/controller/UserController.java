package com.happy_hao.rums.controller;

import com.happy_hao.rums.common.Result;
import com.happy_hao.rums.dto.*;
import com.happy_hao.rums.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

//    private final RestClient restClient;
//
//    public UserController(RestClient restClient) {
//        this.restClient = restClient;
//    }

    @PostMapping("/register/form")
    public Result registerForm(@Valid @RequestBody RegisterRequest registerRequest) {

        return userService.registerForm(registerRequest);
    }

    @PostMapping("/login/form")
    public Result loginForm(@Valid @RequestBody LoginRequest loginRequest) {

        return userService.loginForm(loginRequest);
    }

    @GetMapping("/login/feishu")
    public Result loginFeishu(@RequestParam String code) {

        return userService.loginFeishu(code);
    }

    @GetMapping("manage/userInfo")
    public Result getUserInfo() {
        return userService.userInfo();
    }
}
