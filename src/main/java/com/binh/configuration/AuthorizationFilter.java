package com.binh.configuration;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.binh.exception.TokenInvalidException;
import com.binh.response.ResponseModel;
import com.binh.utils.ApplicationMessage;
import com.binh.utils.DecodeToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Priority;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.HttpHeaders;

@WebFilter("/*")
@Priority(1)
public class AuthorizationFilter implements Filter {

    private static final String ROLE_HEAD_OF_DEPARTMENT = "HEAD_OF_DEPARTMENT";
    private static final String ROLE_TEAM_LEADER = "TEAM_LEADER";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String CONTEXT_PATH = "/agile-assessment";
    private static final Set<String> WHITELIST = new HashSet<>(Set.of(
            CONTEXT_PATH + "/swagger",
            CONTEXT_PATH + "/api/openapi.json"
    ));

    private static final Map<String, Map<String, List<String>>> urlRoleMapping = new HashMap<>() {{
        put(CONTEXT_PATH + "/api/criterias", new HashMap<>() {{
            put(HttpMethod.POST, List.of(ROLE_ADMIN));
        }});
    }};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String requestURL = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        // Allow CORS preflight requests
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // Skip filter for whitelisted endpoints
        if (WHITELIST.stream().anyMatch(requestURL::startsWith)) {
            chain.doFilter(request, response);
            return;
        }

        // Unauthorized - No Token
        if (token == null) {
            ResponseModel<Object> responseModel = ResponseModel.builder()
                    .message(ApplicationMessage.UNAUTHORIZED)
                    .build();

            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(responseModel));
            return;
        }

        // Invalid Token
        String role;
        try {
            role = DecodeToken.getRoleToken(token);
            long expirationTime = DecodeToken.getExpirationToken(token);
            if (expirationTime < new Date().getTime()) {
                throw new TokenInvalidException(ApplicationMessage.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            ResponseModel<Object> responseModel = ResponseModel.builder()
                    .message(exception.getMessage())
                    .build();
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(responseModel));
            return;
        }

        // Forbidden
        if (isAuthorized(requestURL, method, role)) {
            chain.doFilter(request, response);
        } else {
            ResponseModel<Object> responseModel = ResponseModel.builder()
                    .message(ApplicationMessage.FORBIDDEN)
                    .build();

            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(responseModel));
        }
    }

    private boolean isAuthorized(String url, String method, String role) {
        return urlRoleMapping
                .entrySet()
                .stream()
                .filter(entry -> url.matches(entry.getKey()))
                .findFirst()
                .map(entry -> entry.getValue().getOrDefault(method, List.of()).contains(role))
                .orElse(true);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}