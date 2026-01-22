package com.avion.billing_system.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CurrentEmployeeOrders {
    private Long id;

    private String customerName;
    private String mobileNumber;
    private LocalDateTime orderDate = LocalDateTime.now();
    private double totalAmount;

    //    @ManyToOne
//    @JoinColumn(name = "employee_id")
    private Long employeeId;
    private List<CurrentEmployeeOrders.ItemDetails> items;

    @Data
    public static class ItemDetails {
        private String foodItemName;
        private double foodItemPrice;
        private int quantity;
        private double total;
    }
}

