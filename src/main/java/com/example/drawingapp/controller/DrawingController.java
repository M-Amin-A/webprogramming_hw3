package com.example.drawingapp.controller;

import com.example.drawingapp.dto.DrawingRequest;
import com.example.drawingapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/drawing")
@CrossOrigin(origins = "*")
public class DrawingController {
    
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getDrawing() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            String drawing = userService.getDrawing(username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("drawing", drawing);
            response.put("username", username);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateDrawing(@Valid @RequestBody DrawingRequest drawingRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            userService.updateDrawing(username, drawingRequest.getDrawing());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Drawing updated successfully");
            response.put("drawing", drawingRequest.getDrawing());
            response.put("username", username);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}