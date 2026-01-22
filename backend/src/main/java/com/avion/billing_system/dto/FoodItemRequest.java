package com.avion.billing_system.dto;

import lombok.Data;

@Data
public class FoodItemRequest {
    private String name;
    private double price;
    private String imageUrl;

}
