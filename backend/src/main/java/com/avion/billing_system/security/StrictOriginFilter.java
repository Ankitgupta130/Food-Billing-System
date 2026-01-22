//package com.avion.billing_system.security;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class StrictOriginFilter implements Filter {
//
//    private static final String ALLOWED_ORIGIN = "http://localhost:4200";
//
//
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        String origin = httpRequest.getHeader("origin");
//        String uri = httpRequest.getRequestURI();
//
//
//        if ((origin == null || !origin.equals(ALLOWED_ORIGIN)) && !uri.startsWith("/uploads/")) {
//            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            httpResponse.getWriter().write("Forbidden: Access Denied From Backend Server");
//            return;
//        }
//
//
//        chain.doFilter(request, response);
//    }
//
//}