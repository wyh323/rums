package com.happy_hao.rums.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "用户名/邮箱/手机号不能为空")
    @Pattern(regexp = "^(?![_.-])[a-zA-Z0-9_.+-]+(?<![_.-])@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]{2,}$|^1[3-9]\\d{9}$|^(?=.*[a-zA-Z])[a-zA-Z0-9_]{5,20}$", message = "用户名必须是5-20位的字母、数字或下划线且至少包含一个字母/邮箱格式不合法/手机号格式不合法")
    private String identifier;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{8,20}$", message = "密码必须是8-20位，包含字母和数字")
    private String password;
}
