package com.avion.billing_system.dto;


import com.avion.billing_system.entity.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class Userdto {


    private Long id;

    private String username;

    private String password;

    private Role role;


    private String status;


}
