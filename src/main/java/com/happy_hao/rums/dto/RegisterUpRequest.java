package com.happy_hao.rums.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterUpRequest {
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9_]{5,20}$", message = "用户名必须是5-20位的字母、数字或下划线且至少包含一个字母")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{8,20}$", message = "密码必须是8-20位，包含字母和数字")
    private String password;
}
