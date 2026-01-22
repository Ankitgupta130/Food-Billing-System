package com.avion.billing_system.controller;


import com.avion.billing_system.dto.EmployeeRequest;
import com.avion.billing_system.dto.EmployeeResponse;
import com.avion.billing_system.entity.Employee;
import com.avion.billing_system.mapper.EmployeeMapper;
import com.avion.billing_system.repository.EmployeeRepository;
import com.avion.billing_system.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @PostMapping
    public EmployeeResponse createEmployee(@RequestBody EmployeeRequest request) {
        Employee employee = employeeService.registerEmployee(
                request.getName(),
                request.getPhone(),
                request.getEmail(),
                request.getUsername(),
                request.getPassword(),
                request.getAdminId()
        );
        return EmployeeMapper.toDTO(employee);
    }

    @GetMapping
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees()
                .stream()
                .map(EmployeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest request) {

        Employee saved = employeeService.updateEmployee(id, request);
        return EmployeeMapper.toDTO(saved);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username){
        boolean exists = employeeService.usernameExists(username);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Long> getEmployeeIdByUserId(@PathVariable Long userId) {
        return employeeRepository.findByUser_Id(userId)
                .map(employee ->ResponseEntity.ok(employee.getId()))
                .orElseThrow(()-> new RuntimeException("Employees not found"));
    }

}
