package com.happy_hao.rums.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterEpRequest {
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^(?![_.-])[a-zA-Z0-9_.+-]+(?<![_.-])@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]{2,}$", message = "邮箱格式不合法")
    private String email;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "6位验证码")
    private String verificationCode;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{8,20}$", message = "密码必须是8-20位，包含字母和数字")
    private String password;
}
