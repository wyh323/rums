package com.happy_hao.rums.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy_hao.rums.mapper.UserMapper;
import com.happy_hao.rums.po.User;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FormUtil {

    @Resource
    private UserMapper userMapper;

    public User getUser(String identifier) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user;
        // 判断标识符是用户名、邮箱还是手机号
        if (isEmail(identifier)) {
            user = userMapper.selectOne(queryWrapper.eq("email", identifier));
        } else if (isPhone(identifier)) {
            user = userMapper.selectOne(queryWrapper.eq("phone", identifier));
        } else {
            System.out.println(identifier);
            user = userMapper.selectOne(queryWrapper.eq("username", identifier));
        }
        if (Objects.isNull(user)) {
            return null;
        }
        return user;
    }

    public boolean isEmail(String identifier) {
        return identifier.contains("@");
    }

    public boolean isPhone(String identifier) {
        return identifier.matches("^\\d+$");
    }
}
