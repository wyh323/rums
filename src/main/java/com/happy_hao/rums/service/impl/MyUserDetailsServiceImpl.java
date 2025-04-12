package com.happy_hao.rums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy_hao.rums.exception.ServiceException;
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
        User user = formUtil.getUser(identifier);
        if (Objects.isNull(user)) {
            throw new ServiceException("用户不存在");
        }
        return user;
    }
}
