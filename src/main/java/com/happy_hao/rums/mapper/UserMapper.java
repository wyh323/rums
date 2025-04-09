package com.happy_hao.rums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happy_hao.rums.po.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {}
