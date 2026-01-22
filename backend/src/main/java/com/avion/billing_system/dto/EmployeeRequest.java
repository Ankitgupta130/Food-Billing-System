package com.avion.billing_system.dto;

import lombok.Data;

@Data
public class EmployeeRequest {
    private String name;
    private String phone;
    private String email;
    private String username;
    private String password;
    private Long adminId;

}
