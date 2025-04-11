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

    private final RestClient restClient;

    public UserController(RestClient restClient) {
        this.restClient = restClient;
    }

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

    @GetMapping("manage/userInfo")
    public Result getUserInfo() {
        return userService.userInfo();
    }

    @GetMapping("/manage/messages")
    public String messages(Model model,
                           @RegisteredOAuth2AuthorizedClient("feishu") OAuth2AuthorizedClient authorizedClient,
                           @AuthenticationPrincipal OAuth2User oauth2User) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication principal: " + authentication.getPrincipal());
        System.out.println("oauth2User: " + oauth2User);

        if (oauth2User == null) {
            System.out.println("OAuth2User is null");
            return "redirect:/login";
        }

        model.addAttribute("username", oauth2User.getAttribute("name"));
        model.addAttribute("phone", oauth2User.getAttribute("mobile"));
        model.addAttribute("client", authorizedClient.getClientRegistration().getClientName());

        return "UserInfo";
    }
}
