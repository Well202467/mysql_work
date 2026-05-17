package com.example.smartlearning.interceptor;

import com.example.smartlearning.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private static final String ADMIN_TOKEN = "admin-token";
    private static final String LOGIN_PATH = "/api/admin/login";
    private static final String OPTIONS_METHOD = "OPTIONS";

    private final ObjectMapper objectMapper;

    public AdminAuthInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (OPTIONS_METHOD.equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        if (LOGIN_PATH.equals(getRequestPath(request))) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (ADMIN_TOKEN.equals(token)) {
            return true;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.unauthorized("未登录或 token 无效")));
        return false;
    }

    private String getRequestPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (StringUtils.hasText(contextPath) && path.startsWith(contextPath)) {
            return path.substring(contextPath.length());
        }
        return path;
    }
}
