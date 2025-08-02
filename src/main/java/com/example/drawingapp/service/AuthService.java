package com.example.drawingapp.service;

import com.example.drawingapp.dto.AuthResponse;
import com.example.drawingapp.dto.LoginRequest;
import com.example.drawingapp.dto.LogoutRequest;
import com.example.drawingapp.dto.RegisterRequest;
import com.example.drawingapp.entity.User;
import com.example.drawingapp.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {
        User user = userService.registerUser(
            registerRequest.getUsername(), 
            registerRequest.getPassword()
        );
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();
        
        String token = jwtUtils.generateToken(userDetails);
        return new AuthResponse(token, user.getUsername());
    }

    public AuthResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtils.generateToken(userDetails);
            
            return new AuthResponse(token, userDetails.getUsername());
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    public AuthResponse logout(LogoutRequest logoutRequest) {
        try {
            jwtUtils.invalidate_token(logoutRequest.getToken());

            return new AuthResponse();
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
} 