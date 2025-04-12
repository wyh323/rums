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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    @Override
    public Result registerUp(RegisterUpRequest registerUpRequest) {

        if (this.getOne(new QueryWrapper<User>().eq("username", registerUpRequest.getUsername())) != null) {
            throw new ServiceException("用户名已被注册");
        }

        User user = new User();
        user.setUserId(SnowFlakeUtil.getSnowFlakeId());
        user.setUsername(registerUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerUpRequest.getPassword()));
        user.setEnabled(true);
        user.setCreateAt(new Date());
        this.save(user);

        return Result.success().message("注册成功");
    }

    @Override
    public Result registerEp(RegisterEpRequest registerEpRequest) {

        if (this.getOne(new QueryWrapper<User>().eq("email", registerEpRequest.getEmail())) != null) {
            throw new ServiceException("邮箱已被注册");
        }

        if (!registerEpRequest.getVerificationCode().equals(registerEpRequest.getVerificationCode())) {
            throw new ServiceException("验证码不正确");
        }

        User user = new User();
        user.setUserId(SnowFlakeUtil.getSnowFlakeId());
        String e = generateRandomString(20);
        while (this.getOne(new QueryWrapper<User>().eq("username", e)) != null) {
            e = generateRandomString(20);
        }
        user.setUsername(e);
        user.setEmail(registerEpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerEpRequest.getPassword()));
        user.setEnabled(true);
        user.setCreateAt(new Date());
        this.save(user);

        return Result.success().message("注册成功");
    }

    @Override
    public Result registerPh(RegisterPhRequest registerPhRequest) {
        if (this.getOne(new QueryWrapper<User>().eq("phone", registerPhRequest.getPhone())) != null) {
            throw new ServiceException("电话号码已被注册");
        }

        if (!registerPhRequest.getVerificationCode().equals(registerPhRequest.getVerificationCode())) {
            throw new ServiceException("验证码不正确");
        }

        User user = new User();
        user.setUserId(SnowFlakeUtil.getSnowFlakeId());
        String e = generateRandomString(20);
        while (this.getOne(new QueryWrapper<User>().eq("username", e)) != null) {
            e = generateRandomString(20);
        }
        user.setUsername(e);
        user.setPhone(registerPhRequest.getPhone());
        user.setPassword(passwordEncoder.encode(registerPhRequest.getPassword()));
        user.setEnabled(true);
        user.setCreateAt(new Date());
        this.save(user);

        return Result.success().message("注册成功");
    }

    @Override
    public Result registerFs(String code) {
        String phone = getPhoneByCode(code);
        if (phone == null) {
            throw new ServiceException("失败");
        }

        if (this.getOne(new QueryWrapper<User>().eq("phone", phone)) != null) {
            throw new ServiceException("电话号码已被注册");
        }

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

        return Result.success().message("注册成功,初始密码w12345678,请尽快修改");
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
        User user = (User) userDetailsService.loadUserByUsername(getPhoneByCode(code));
        return Result.success().data("user", user);
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
