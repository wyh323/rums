package com.happy_hao.rums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy_hao.pdsds_backend.entity.User;
import com.happy_hao.pdsds_backend.exception.ServiceException;
import com.happy_hao.pdsds_backend.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User loadUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new ServiceException("该用户不存在");
        }
        return user;
    }
}
