package com.avion.billing_system.dto;

import lombok.Data;

@Data
public class FoodItemResponse {

    private Long id;
    private String name;
    private double price;
    private String imageUrl;

}
