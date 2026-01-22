package com.avion.billing_system.repository;

import com.avion.billing_system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByUser_Username(String username) ;
    Optional<Employee> findByUser_Id(Long userId);
}
