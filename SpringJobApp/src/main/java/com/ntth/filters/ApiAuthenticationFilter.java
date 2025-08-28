/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntth.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author LOQ
 */
public class ApiAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private JwtUtils jwtUtil;

    public ApiAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
            response.setContentType("application/json");
            try {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Method not allowed. Use POST.");
                response.getWriter().write(objectMapper.writeValueAsString(error));
            } catch (IOException e) {
                // Ignore
            }
            return null;
        }

        try {
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);
            String username = credentials.get("username");
            String password = credentials.get("password");
            System.out.println("[DEBUG] Nhận yêu cầu đăng nhập: username=" + username + ", password length=" + (password != null ? password.length() : 0));
            if (username == null || password == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setContentType("application/json");
                Map<String, String> error = new HashMap<>();
                error.put("error", "Missing username or password");
                response.getWriter().write(objectMapper.writeValueAsString(error));
                return null;
            }
            System.out.println("[DEBUG] API login attempt: " + username);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    username, password);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            try {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid JSON format");
                response.getWriter().write(objectMapper.writeValueAsString(error));
            } catch (IOException ex) {
                // Ignore
            }
            return null;
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", false);
        responseBody.put("message", failed.getMessage() != null ? failed.getMessage() : "Xác thực thất bại.");
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }

}
