package com.avion.billing_system.service;


import com.avion.billing_system.dto.EmployeeRequest;
import com.avion.billing_system.entity.Employee;
import com.avion.billing_system.entity.Role;
import com.avion.billing_system.entity.User;
import com.avion.billing_system.repository.EmployeeRepository;
import com.avion.billing_system.repository.UserRepository;
import com.avion.billing_system.util.MD5Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;



    private static final String EMPLOYEE_CACHE_KEY = "ALL_EMPLOYEES";

    public Employee registerEmployee(String name, String phone, String email, String username, String rawPassword, Long adminId) {
        if(userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");

        }
        String hashedPassword = MD5Util.encrypt(rawPassword);

        User user  = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setRole(Role.EMPLOYEE);

        User savedUser = userRepository.save(user);

        Employee employee = new Employee();
        employee.setName(name);
        employee.setPhone(phone);
        employee.setEmail(email);
        employee.setUser(savedUser);
        employee.setCreatedBy(adminId);

        redisTemplate.delete(EMPLOYEE_CACHE_KEY);

        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        Object cachedData = redisTemplate.opsForValue().get(EMPLOYEE_CACHE_KEY);

        if (cachedData !=null) {
            System.out.println("Returning employees from Redis cache");
            return objectMapper.convertValue(
                    cachedData,
                    new com.fasterxml.jackson.core.type.TypeReference<List<Employee>>() {}
            );
        }


        List<Employee> all = employeeRepository.findAll();






        redisTemplate.opsForValue().set(EMPLOYEE_CACHE_KEY, all, Duration.ofDays(1));

        return all;
    }

    public Employee updateEmployee(Long id, EmployeeRequest request) {
        return employeeRepository.findById(id).map(emp ->{
            emp.setName(request.getName());
            emp.setPhone(request.getPhone());
            emp.setEmail(request.getEmail());

            if(request.getUsername() != null) {
                User user = emp.getUser();
                user.setUsername(request.getUsername());
                userRepository.save(user);
            }

            redisTemplate.delete(EMPLOYEE_CACHE_KEY);

            return employeeRepository.save(emp);
        }).orElseThrow(()-> new RuntimeException("Employee not found with id:"+id));
    }

    public void deleteEmployee(Long id) {
        employeeRepository.findById(id).ifPresent(emp-> {
            Long userId = emp.getUser().getId();
            employeeRepository.delete(emp);
            userRepository.deleteById(userId);

            redisTemplate.delete(EMPLOYEE_CACHE_KEY);
        });
    }

    public boolean usernameExists(String username) {
        return employeeRepository.existsByUser_Username(username);
    }


}
