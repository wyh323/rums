package com.happy_hao.rums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy_hao.rums.common.Result;
import com.happy_hao.rums.dto.LoginRequest;
import com.happy_hao.rums.dto.RegistrationRequest;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtil jwtUtil;


    @Override
    public Result register(RegistrationRequest registrationRequest) {

        if (this.getOne(new QueryWrapper<User>().eq("username", registrationRequest.getUsername())) != null) {
            throw new ServiceException("用户名已被注册");
        }

        User user = new User();
        user.setUserId(SnowFlakeUtil.getSnowFlakeId());
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setEnabled(true);
        user.setCreateAt(LocalDateTime.now());
        this.save(user);

        return Result.success().message("注册成功");
    }

    @Override
    public Result login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
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
}
