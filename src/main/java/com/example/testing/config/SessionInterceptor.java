package com.example.testing.config;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/css/") || requestURI.startsWith("/js/") || requestURI.startsWith("/images/")) {
            return true;
        }
        if (session != null && session.getAttribute("user") != null) {
            if (requestURI.equals("/users/login") || requestURI.equals("/users/register")) {
                response.sendRedirect("/students/home");
                return false;
            }
        } else {
            if (!requestURI.equals("/users/login") && !requestURI.equals("/users/register")) {
                response.sendRedirect("/users/login");
                return false;
            }
        }
        return true;
    }
}
