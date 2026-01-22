package com.avion.billing_system.repository;

import com.avion.billing_system.entity.FoodItem;
import org.aspectj.apache.bcel.util.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
}
