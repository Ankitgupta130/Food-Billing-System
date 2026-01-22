package com.avion.billing_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String customerName;
    private String mobileNumber;
    private Long employeeId;
    private double totalAmount;

    private List<CartItemDTO> cartItems;

    @Data
    public static class CartItemDTO {
        private Long foodItemId;
        private int quantity;

    }
}
