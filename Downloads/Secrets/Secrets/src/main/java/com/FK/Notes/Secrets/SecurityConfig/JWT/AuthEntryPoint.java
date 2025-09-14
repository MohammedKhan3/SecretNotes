package com.FK.Notes.Secrets.SecurityConfig.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * Custom authentication entry point that handles unauthorized access attempts.
 *
 *
 * This class is triggered whenever an unauthenticated user tries to access a
 * secured resource in the application. Instead of returning the default HTML error,
 * it returns a JSON response with error details.
 *
 */
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    logger.error("unauthorized error: ",authException.getMessage()); //log the unauthorized error
    response.setContentType(MediaType.APPLICATION_JSON_VALUE); //set response type and status
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    final Map<String,Object> body = new HashMap<>(); //build response body with error and details
    body.put("status",HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error","unauthorized");
    body.put("message",authException.getMessage());
    body.put("path",request.getServletPath());

    final ObjectMapper mapper = new ObjectMapper();//write a response as a json.
    mapper.writeValue(response.getOutputStream(),body);

    }
}
