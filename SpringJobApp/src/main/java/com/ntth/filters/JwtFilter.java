
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.filters;

import com.ntth.util.JwtUtils;
import com.ntth.util.JwtUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author huu-thanhduong
 */
public class JwtFilter implements Filter {

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        if (httpRequest.getRequestURI().startsWith(String.format("%s/api/followed", httpRequest.getContextPath()))) {
//            String header = httpRequest.getHeader("Authorization");
//            if (header == null || !header.startsWith("Bearer ")) {
//                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
//                return;
//            }
//            String token = header.substring(7);
//            try {
//                String username = JwtUtils.validateTokenAndGetUsername(token);
//                if (username != null) {
//                    httpRequest.setAttribute("username", username);
//                    // Thêm kiểm tra role nếu cần
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    chain.doFilter(request, response);
//                    return;
//                }
//            } catch (Exception e) {
//                // Log lỗi
//            }
//            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ hoặc hết hạn");
//        }
//        chain.doFilter(request, response);
//    }
    private static final List<String> SECURE_API_PATHS = List.of(
            "/api/jobapplications",
            "/api/addapplication",
            "/api/jobpostings",
            "/api/feedback/add/",
            "/api/follow",
            "/api/followed",
            "/api/worked-with"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        addCorsHeaders(httpRequest, httpResponse); // ✅ Thêm CORS headers luôn

        // Nếu là preflight request (OPTIONS), trả về luôn
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        String contextPath = httpRequest.getContextPath(); // ví dụ: "/JobSearchWebApp"
        String requestURI = httpRequest.getRequestURI();   // ví dụ: "/JobSearchWebApp/api/followed"

        boolean requiresAuth = SECURE_API_PATHS.stream()
                .anyMatch(apiPath -> requestURI.startsWith(contextPath + apiPath));

        if (requiresAuth) {

            String header = httpRequest.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
                return;
            }

            String token = header.substring(7);
            try {
                String username = JwtUtils.validateTokenAndGetUsername(token);
                if (username != null) {
                    UsernamePasswordAuthenticationToken authentication
                            = new UsernamePasswordAuthenticationToken(username, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    chain.doFilter(request, response);
                    return;
                }
                if (username == null) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ hoặc hết hạn");
                    return;
                }
            } catch (Exception e) {
                // Log lỗi nếu cần
            }
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ hoặc hết hạn");
            return;
        }

        chain.doFilter(request, response);
    }

    private void addCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");

    }
}
