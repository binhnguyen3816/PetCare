package com.binh.configuration;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class JwtTokenFilter implements Filter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_ATTRIBUTE = "currentToken";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader(AUTH_HEADER);
        if (token != null && token.startsWith("Bearer ")) {
            httpRequest.setAttribute(TOKEN_ATTRIBUTE, token);
        } else {
            httpRequest.setAttribute(TOKEN_ATTRIBUTE, null);
        }
        chain.doFilter(request, response);
    }

    public static String getCurrentToken(HttpServletRequest request) {
        return (String) request.getAttribute(TOKEN_ATTRIBUTE);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}