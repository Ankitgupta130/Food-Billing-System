package com.avion.billing_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private double total;

//    @ManyToOne
//    @JoinColumn(name = "order_id")
    private Long orderId;

    //@ManyToOne
    //@JoinColumn(name = "food_item_id")
    private Long foodItemId;

}
