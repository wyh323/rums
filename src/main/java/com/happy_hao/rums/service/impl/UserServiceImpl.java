package com.happy_hao.rums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy_hao.rums.common.Result;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    public Result login(LoginRequest loginRequest) {
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
}
