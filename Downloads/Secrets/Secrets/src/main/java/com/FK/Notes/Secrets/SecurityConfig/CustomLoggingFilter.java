package com.FK.Notes.Secrets.SecurityConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
/*
This class extends OncePerRequestFilter will - run filter exactly one time.

 */
public class CustomLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Custom Logging Filter : " + request.getRequestURI());
        filterChain.doFilter(request,response);
        System.out.println("Response " + response.getStatus());
    }
}
