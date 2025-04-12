package com.happy_hao.rums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy_hao.rums.po.User;
import com.happy_hao.rums.mapper.UserMapper;
import com.happy_hao.rums.util.FormUtil;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private FormUtil formUtil;

    @Override
    public User loadUserByUsername(String identifier) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user;
        // 判断标识符是用户名、邮箱还是手机号
        if (formUtil.isEmail(identifier)) {
            user = userMapper.selectOne(queryWrapper.eq("email", identifier));
        } else if (formUtil.isPhone(identifier)) {
            user = userMapper.selectOne(queryWrapper.eq("phone", identifier));
        } else {
            user = userMapper.selectOne(queryWrapper.eq("username", identifier));
        }
        if (Objects.isNull(user)) {
            return null;
        }
        return user;
    }
}
