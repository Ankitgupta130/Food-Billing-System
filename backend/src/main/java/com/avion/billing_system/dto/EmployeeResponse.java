package com.avion.billing_system.dto;

import lombok.Data;

@Data
public class EmployeeResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String username;
    private String role;
    private String status;
    private Long createdBy;
    private String createdAt;
    private String UserId;

}
