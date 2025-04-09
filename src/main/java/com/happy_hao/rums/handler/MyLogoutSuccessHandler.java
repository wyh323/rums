package com.happy_hao.rums.handler;

import com.alibaba.fastjson2.JSON;
import com.happy_hao.pdsds_backend.common.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String json = JSON.toJSONString(Result.success().message("登出成功"));
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
