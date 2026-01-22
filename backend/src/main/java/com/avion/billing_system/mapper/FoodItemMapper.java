package com.avion.billing_system.mapper;

import com.avion.billing_system.dto.FoodItemRequest;
import com.avion.billing_system.dto.FoodItemResponse;
import com.avion.billing_system.entity.FoodItem;

public class FoodItemMapper {


    public static FoodItem toEntity(FoodItemRequest req) {
        FoodItem item = new FoodItem();
        item.setName(req.getName());
        item.setPrice(req.getPrice());
        item.setImageUrl(req.getImageUrl());
        return item;
    }

    public static FoodItemResponse toDTO(FoodItem item) {
        FoodItemResponse dto = new FoodItemResponse();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setImageUrl(item.getImageUrl());
        return dto;
    }
}
