package com.avion.billing_system.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "snack")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Snack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long snackId;

    @Column(nullable = false, unique = true)
    private String name;
}
