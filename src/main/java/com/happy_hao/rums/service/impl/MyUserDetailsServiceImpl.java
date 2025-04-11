package com.happy_hao.rums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy_hao.rums.po.User;
import com.happy_hao.rums.exception.ServiceException;
import com.happy_hao.rums.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User loadUserByUsername(String identifier) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = null;
        // 判断标识符是用户名、邮箱还是手机号
        if (isEmail(identifier)) {
            user = userMapper.selectOne(queryWrapper.eq("email", identifier));
        } else if (isPhone(identifier)) {
            user = userMapper.selectOne(queryWrapper.eq("phone", identifier));
        } else {
            user = userMapper.selectOne(queryWrapper.eq("username", identifier));
        }
        if (Objects.isNull(user)) {
            throw new ServiceException("该用户不存在");
        }
        return user;
    }

    private boolean isEmail(String identifier) {
        return identifier.contains("@");
    }

    private boolean isPhone(String identifier) {
        return identifier.matches("^\\d+$");
    }
}
