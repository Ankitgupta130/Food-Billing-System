package com.avion.billing_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderHistoryResponse {
    private String orderId;
    private String customerName;
    private String mobileNumber;
    private String employeeId;
    private String employeeName;
    private LocalDateTime orderDate;
    private double totalAmount;
    private List<ItemDetails> items;

    @Data
    public static class ItemDetails{
        private String foodItemName;
        private double foodItemPrice;
        private int quantity;
        private double total;
    }

}
