package com.happy_hao.rums.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User {

    @TableId("user_id")
    private Long userId; // 主键用户ID

    @TableField("username")
    private String username; // 用户名

    @TableField("nickname")
    private String nickname; // 昵称

    @JsonIgnore
    @TableField("password")
    private String password; // 密码

    @Email
    @TableField("email")
    private String email; // 邮箱

    @TableField("phone")
    private String phone; // 电话

    @TableField("gender")
    private String gender; // 性别

    @TableField("avatar")
    private String avatar; // 头像

    @TableField("enabled")
    private boolean enabled; // 激活状态

    @TableField("create_at")
    private LocalDateTime createAt; // 创建时间

    @TableField("update_at")
    private LocalDateTime updateAt; // 更新时间

    @TableField("description")
    private String description;

    @TableField("extension")
    private String extension;

    @TableField(exist = false)
    private List<Group> groups = new ArrayList<>();
}