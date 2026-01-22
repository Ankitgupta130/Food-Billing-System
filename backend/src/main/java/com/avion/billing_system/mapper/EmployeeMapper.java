package com.avion.billing_system.mapper;

import com.avion.billing_system.dto.EmployeeResponse;
import com.avion.billing_system.entity.Employee;
import com.avion.billing_system.entity.User;

import java.time.format.DateTimeFormatter;


public class EmployeeMapper  {

    public static EmployeeResponse toDTO(Employee employee){
        EmployeeResponse dto = new EmployeeResponse();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setPhone(employee.getPhone());
        dto.setEmail(employee.getEmail());
        dto.setUsername(employee.getUser().getUsername());
        dto.setRole(employee.getUser().getRole().name());
        dto.setStatus(employee.getUser().getStatus());
        dto.setCreatedBy(employee.getCreatedBy());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dto.setCreatedAt(employee.getCreatedAt().format(formatter));
        User user1= employee.getUser();
        dto.setUserId(user1.getId().toString());
        return dto;
    }


}
