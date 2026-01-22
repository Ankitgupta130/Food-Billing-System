package com.avion.billing_system.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Long userId;
    private String username;
    private String role;
    private String message;
    private String status;


}
