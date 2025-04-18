package com.happy_hao.rums.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy_hao.rums.common.Result;
import com.happy_hao.rums.config.FeishuConfig;
import com.happy_hao.rums.dto.*;
import com.happy_hao.rums.po.User;
import com.happy_hao.rums.exception.ServiceException;
import com.happy_hao.rums.mapper.UserMapper;
import com.happy_hao.rums.service.UserService;
import com.happy_hao.rums.util.FormUtil;
import com.happy_hao.rums.util.JwtUtil;
import com.happy_hao.rums.util.SnowFlakeUtil;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private FeishuConfig feishuConfig;

    @Resource
    private FormUtil formUtil;

    @Override
    public Result registerForm(RegisterRequest registerRequest) {
        String identifier = registerRequest.getIdentifier();
        User user1 = formUtil.getUser(identifier);
        if (user1 != null) {
            throw new ServiceException("用户名/邮箱/电话已被注册");
        }

        User user = new User();
        user.setUserId(SnowFlakeUtil.getSnowFlakeId());
        String e = generateRandomString(20);
        while (this.getOne(new QueryWrapper<User>().eq("username", e)) != null) {
            e = generateRandomString(20);
        }
        if (formUtil.isEmail(identifier)) {
            user.setUsername(e);
            user.setEmail(identifier);
        } else if (formUtil.isPhone(identifier)) {
            user.setUsername(e);
            user.setPhone(identifier);
        } else {
            user.setUsername(identifier);
        }
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(true);
        user.setCreateAt(new Date());
        this.save(user);

        return Result.success().message("注册成功");
    }

    @Override
    public Result loginForm(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getIdentifier(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 生成 JWT Token 并返回
            User user = (User) authentication.getPrincipal();
            Map<String, Object> claims = new HashMap<>();
            claims.put("user", user);
            String token = jwtUtil.generateToken(claims);
            return Result.success().message("登录成功").data("token", token);
        } catch (AuthenticationException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Result loginFeishu(String code) {
        String phone = getPhoneByCode(code);
        User user1 = formUtil.getUser(phone);
        if (user1 == null) {
            User user = new User();
            user.setUserId(SnowFlakeUtil.getSnowFlakeId());
            String e = generateRandomString(20);
            while (this.getOne(new QueryWrapper<User>().eq("username", e)) != null) {
                e = generateRandomString(20);
            }
            user.setUsername(e);
            user.setPhone(phone);
            user.setPassword(passwordEncoder.encode("w12345678"));
            user.setEnabled(true);
            user.setCreateAt(new Date());
            this.save(user);
            return Result.success().message("注册成功,初始密码为w12345678,请尽快修改");
        }
        return Result.success().message("登录成功").data("user", user1);
    }

    @Override
    public Result userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ServiceException("请登录1");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof OAuth2User oauth2User)) {
            User user = (User) principal;
            return Result.success().data("user", user);
        }

        Map<String, Object> userAttributes = oauth2User.getAttributes();

        String phone = ((String) userAttributes.get("mobile")).replace("+86", "");
        User user = (User) userDetailsService.loadUserByUsername(phone);

        return Result.success().data("user", user);
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public String getPhoneByCode(String code) {
        // 获取飞书 token
        String token = getTokenByCode(code);

        // 获取用户信息
        return getUserInfoByToken(token);
    }

    private String getTokenByCode(String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", feishuConfig.getClientId());
        params.put("client_secret", feishuConfig.getClientSecret());
        params.put("code", code);
        params.put("redirect_uri", feishuConfig.getRedirectUri());

        String result = HttpUtil.createPost(feishuConfig.getTokenUri())
                .header("Content-Type", "application/json")
                .charset("utf-8")
                .body(JSONUtil.toJsonStr(params))
                .execute()
                .body();

        if (!StringUtils.hasText(result)) {
            throw new ServiceException("获取飞书 token 失败，响应为空");
        }

        JSONObject parseObj = JSONUtil.parseObj(result);
        if ((int) parseObj.get("code") != 0) {
            throw new ServiceException("请求飞书 token 失败：" + parseObj.get("error_description"));
        }

        String userAccessToken = parseObj.getStr("access_token");
        String tokenType = parseObj.getStr("token_type");

        return tokenType + " " + userAccessToken;
    }

    private String getUserInfoByToken(String token) {
        String result = HttpUtil.createGet(feishuConfig.getUserInfoUri())
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .charset("utf-8")
                .execute()
                .body();

        if (!StringUtils.hasText(result)) {
            throw new ServiceException("请求飞书用户详情接口失败，响应为空");
        }

        JSONObject parseObj = JSONUtil.parseObj(result);
        return parseObj.getStr("mobile").replace("+86", "");
    }

}
