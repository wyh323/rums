package com.happy_hao.rums.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegistrationRequest {
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{5,20}$", message = "用户名必须是5-20位的字母、数字或下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{8,20}$", message = "密码必须是8-20位，包含字母和数字")
    private String password;

    @NotBlank(message = "角色不能为空")
    @Pattern(regexp = "^DOCTOR|PATIENT$", message = "角色必须是医生或者患者")
    private String role;
}
