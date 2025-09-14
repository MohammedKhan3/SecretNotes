package com.FK.Notes.Secrets.Controllers;

import com.FK.Notes.Secrets.DTOs.LoginRequest;
import com.FK.Notes.Secrets.DTOs.LoginResponse;
import com.FK.Notes.Secrets.SecurityConfig.JWT.AuthEntryPoint;
import com.FK.Notes.Secrets.SecurityConfig.JWT.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/*
The SecurityContext is a container (a holder object) that stores security-related information about the current request/session.

Most importantly, it stores the Authentication object, which represents the currently logged-in user.

 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
     JwtUtils jwtUtils;/// Utility class for generating/validating JWT tokens

    @Autowired
    AuthenticationManager authenticationManager;// Handles authentication logic


@PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
    Authentication authentication;
    try{
        // Authenticate user with username and password
     authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));

    }catch (ArithmeticException exception){
     Map<String,Object> map = new HashMap<>();  // Catch failed authentication and return a custom response
     map.put("message","bad credentials");
     map.put("status",false);
     return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }
    // Store the successful authentication in the security context
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();    // Extract user details from the authentication object

    String jwtToken = jwtUtils.generateTokenfromUserName(userDetails); // Generate a JWT token for the authenticated user
    List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()); // Convert GrantedAuthority objects into a list of role string
    LoginResponse loginResponse = new  LoginResponse(userDetails.getUsername(),roles,jwtToken);  // Create a custom login response object with username, roles, and token
    return ResponseEntity.ok(loginResponse);   // Return response with 200 OK and the login response body

}

}
