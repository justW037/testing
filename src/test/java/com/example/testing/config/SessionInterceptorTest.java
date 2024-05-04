package com.example.testing.config;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.http.HttpSession;


public class SessionInterceptorTest {
    @Test
    public void testPreHandle_LoggedInUser_AtLoginPage_RedirectToHome() throws Exception {
        // Arrange
        SessionInterceptor interceptor = new SessionInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        HttpSession session = request.getSession();
        session.setAttribute("user", "loggedInUser");
        request.setRequestURI("/users/login");

        // Act
        boolean result = interceptor.preHandle(request, response, null);

        // Assert
        assertFalse(result);
        assertEquals("/students/home", response.getRedirectedUrl());
    }

    @Test
    public void testPreHandle_LoggedInUser_AtRegisterPage_RedirectToHome() throws Exception {
        // Arrange
        SessionInterceptor interceptor = new SessionInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        HttpSession session = request.getSession();
        session.setAttribute("user", "loggedInUser");
        request.setRequestURI("/users/register");

        // Act
        boolean result = interceptor.preHandle(request, response, null);

        // Assert
        assertFalse(result);
        assertEquals("/students/home", response.getRedirectedUrl());
    }

    @Test
    public void testPreHandle_NotLoggedInUser_AtLoginPage_PassThrough() throws Exception {
        // Arrange
        SessionInterceptor interceptor = new SessionInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/users/login");

        // Act
        boolean result = interceptor.preHandle(request, response, null);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testPreHandle_NotLoggedInUser_AtRegisterPage_PassThrough() throws Exception {
        // Arrange
        SessionInterceptor interceptor = new SessionInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/users/register");

        // Act
        boolean result = interceptor.preHandle(request, response, null);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testPreHandle_StaticResources_PassThrough() throws Exception {
        // Arrange
        SessionInterceptor interceptor = new SessionInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/css/styles.css");

        // Act
        boolean result = interceptor.preHandle(request, response, null);

        // Assert
        assertTrue(result);
    }

}
