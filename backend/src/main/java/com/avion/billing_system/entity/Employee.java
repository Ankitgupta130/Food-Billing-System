package com.avion.billing_system.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long createdBy;

    private LocalDateTime createdAt = LocalDateTime.now();






}
