package com.example.drawingapp.dto;

import jakarta.validation.constraints.NotBlank;

public class DrawingRequest {
    
    @NotBlank(message = "Drawing data is required")
    private String drawing;
    
    public DrawingRequest() {}
    
    public DrawingRequest(String drawing) {
        this.drawing = drawing;
    }
    
    public String getDrawing() {
        return drawing;
    }
    
    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }
} 