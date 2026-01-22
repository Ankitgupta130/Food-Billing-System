package com.avion.billing_system.dto;

import jakarta.persistence.JoinColumn;
import lombok.Data;

@Data
public class OrderResponse {
    private String customerName;
    private String mobileNumber;

    private Long employeeId;
    private double totalAmount;
}
