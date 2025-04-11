package com.happy_hao.rums.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User implements UserDetails {

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

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("create_at")
    private Date createAt; // 创建时间

    @TableField("update_at")
    private Date updateAt; // 更新时间

    @TableField("description")
    private String description;

    @TableField("extension")
    private String extension;

    @TableField(exist = false)
    private List<Group> groups = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return groups.stream()
                .map(role -> new SimpleGrantedAuthority(role.getGroupName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}