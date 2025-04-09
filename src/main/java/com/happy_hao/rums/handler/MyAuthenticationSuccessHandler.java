package com.happy_hao.rums.handler;

import com.alibaba.fastjson2.JSON;
import com.happy_hao.pdsds_backend.common.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Object principle = authentication.getPrincipal();

        String json = JSON.toJSONString(Result.success().message("登录成功").data("user", principle));

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
