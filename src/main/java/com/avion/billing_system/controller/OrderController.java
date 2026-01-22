package com.avion.billing_system.controller;

import com.avion.billing_system.dto.CurrentEmployeeOrders;
import com.avion.billing_system.dto.OrderHistoryResponse;
import com.avion.billing_system.dto.OrderRequest;
import com.avion.billing_system.repository.projection.CurrentOrdersEmp;
import com.avion.billing_system.repository.projection.OrderHistoryProjection;
import com.avion.billing_system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/place-order")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {

        orderService.placeOrder(request);
        return ResponseEntity.ok("Order place successfully");
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderHistoryResponse>> getOrderHistory() {
        return ResponseEntity.ok(orderService.getOrderHistory());
    }

    @GetMapping("/filter")
    public List<OrderHistoryResponse> getOrderHistoryBetweenDates(@RequestParam String startDate, @RequestParam String endDate) {
        return orderService.getOrderHistoryBetweenDates(startDate, endDate);
    }

    @GetMapping("/employee")
    public List<OrderHistoryResponse> getOrdersByEmployee(@RequestParam Long employeeId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
         return orderService.getOrdersOfCurrentEmployee(employeeId,startDate, endDate);

    }

}

