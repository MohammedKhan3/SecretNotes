package com.FK.Notes.Secrets.Controllers;

import com.FK.Notes.Secrets.DTOs.*;
import com.FK.Notes.Secrets.Models.AppRole;
import com.FK.Notes.Secrets.Models.Role;
import com.FK.Notes.Secrets.Models.User;
import com.FK.Notes.Secrets.Repo.RoleRepository;
import com.FK.Notes.Secrets.Repo.UserRepo;
import com.FK.Notes.Secrets.SecurityConfig.JWT.AuthEntryPoint;
import com.FK.Notes.Secrets.SecurityConfig.JWT.JwtUtils;
import com.FK.Notes.Secrets.Service.UserService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.beans.Encoder;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    
    @Autowired
    UserRepo userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

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

@PostMapping("/public/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
    if(userRepository.existsByUserName(signupRequest.getUsername())){
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already in use"));
    }

    if(userRepository.existsByEmail(signupRequest.getEmail())){
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
    }
    User user = new User(signupRequest.getUsername(),signupRequest.getEmail(),encoder.encode(signupRequest.getPassword()));

    Set<String> strRoles = signupRequest.getRole();
    Role role;
    if (strRoles == null || strRoles.isEmpty()) {
        role = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    } else {
        String roleStr = strRoles.iterator().next();
        if (roleStr.equals("admin")) {
            role = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        } else {
            role = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        }

        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        user.setAccountExpiryDate(LocalDate.now().plusYears(1));
        user.setTwoFactorEnabled(false);
        user.setSignUpMethod("email");
    }
    user.setRole(role);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));


    }
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.isTwoFactorEnabled(),
                roles
        );

        return ResponseEntity.ok().body(response);
    }

}
