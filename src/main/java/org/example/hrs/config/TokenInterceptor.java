package org.example.hrs.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.hrs.exception.base.BusinessException;
import org.example.hrs.utils.Utils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if (method.equals("GET")) return true;

        String token = request.getHeader("Authorization").split("Bearer ")[1];
        if (Utils.isTokenExpired(token)) {
            throw new BusinessException("Token is expired");
        }
        return true;
    }
}
