package com.avion.billing_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String mobileNumber;
    private LocalDateTime orderDate = LocalDateTime.now();
    private double totalAmount;

//    @ManyToOne
//    @JoinColumn(name = "employee_id")
    private Long employeeId;




}
