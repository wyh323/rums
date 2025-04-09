package com.happy_hao.rums.handler;

import com.alibaba.fastjson2.JSON;
import com.happy_hao.pdsds_backend.common.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String json = JSON.toJSONString(Result.error().message("您尚未登录"));
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
