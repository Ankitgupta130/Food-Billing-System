package com.avion.billing_system.service;

import com.avion.billing_system.dto.OrderHistoryResponse;
import com.avion.billing_system.dto.OrderRequest;
import com.avion.billing_system.entity.FoodItem;
import com.avion.billing_system.entity.Order;
import com.avion.billing_system.entity.OrderDetail;
import com.avion.billing_system.repository.EmployeeRepository;
import com.avion.billing_system.repository.FoodItemRepository;
import com.avion.billing_system.repository.OrderDetailRepository;
import com.avion.billing_system.repository.OrderRepository;
import com.avion.billing_system.dto.CurrentEmployeeOrders;
import com.avion.billing_system.repository.projection.CurrentOrdersEmp;
import com.avion.billing_system.repository.projection.OrderHistoryProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final FoodItemRepository foodItemRepository;
    private final EmployeeRepository employeeRepository;

    public Order placeOrder(OrderRequest input) {


        Order order = new Order();
        order.setCustomerName(input.getCustomerName());
        order.setMobileNumber(input.getMobileNumber());
        order.setEmployeeId(input.getEmployeeId());
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(input.getTotalAmount());

        Order saveOrder = orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();


        for (OrderRequest.CartItemDTO cartItem : input.getCartItems()) {
            FoodItem foodItem = foodItemRepository.findById(cartItem.getFoodItemId())
                    .orElseThrow(() -> new RuntimeException("Food Item not found"));

            OrderDetail detail = new OrderDetail();
            detail.setFoodItemId(foodItem.getId());
            detail.setQuantity(cartItem.getQuantity());
            detail.setTotal(foodItem.getPrice() * cartItem.getQuantity());
            detail.setOrderId(saveOrder.getId());

            orderDetails.add(detail);
        }

        orderDetailRepository.saveAll(orderDetails);

//        saveOrder.setOrderDetails(orderDetails);
        return saveOrder;

    }

    public List<OrderHistoryResponse> getOrderHistory() {
        List<OrderHistoryProjection> flatData = orderRepository.getOrderHistory();

        Map<Long, OrderHistoryResponse> orderMap = new LinkedHashMap<>();

        for (OrderHistoryProjection projection : flatData) {
            Long orderId = projection.getOrderId();

            orderMap.computeIfAbsent(orderId, id -> {
                OrderHistoryResponse order = new OrderHistoryResponse();
                order.setOrderId(String.valueOf(id));
                order.setCustomerName(projection.getCustomerName());
                order.setMobileNumber(projection.getMobileNumber());
                order.setEmployeeId(projection.getEmployeeId());
                order.setEmployeeName(projection.getEmployeeName());
                order.setOrderDate(projection.getOrderDate());
                order.setTotalAmount(projection.getOrderTotal());
                order.setItems(new ArrayList<>());
                return order;
            });

            OrderHistoryResponse.ItemDetails item = new OrderHistoryResponse.ItemDetails();
            item.setFoodItemName(projection.getFoodName());
            item.setFoodItemPrice(projection.getFoodPrice());
            item.setQuantity(projection.getQuantity());
            item.setTotal(projection.getItemTotal());

            orderMap.get(orderId).getItems().add(item);

        }

        return new ArrayList<>(orderMap.values());
    }

    public List<OrderHistoryResponse> getOrderHistoryBetweenDates(String startDate, String endDate) {
        List<OrderHistoryProjection> flatData = orderRepository.getOrderHistoryBetweenDates(startDate, endDate);

        Map<Long, OrderHistoryResponse> orderMap = new LinkedHashMap<>();

        for (OrderHistoryProjection projection : flatData) {
            Long orderId = projection.getOrderId();

            orderMap.computeIfAbsent(orderId, id -> {
                OrderHistoryResponse order = new OrderHistoryResponse();
                order.setOrderId(String.valueOf(id));
                order.setCustomerName(projection.getCustomerName());
                order.setMobileNumber(projection.getMobileNumber());
                order.setEmployeeId(projection.getEmployeeId());
                order.setEmployeeName(projection.getEmployeeName());
                order.setOrderDate(projection.getOrderDate());
                order.setTotalAmount(projection.getOrderTotal());
                order.setItems(new ArrayList<>());
                return order;
            });

            OrderHistoryResponse.ItemDetails item = new OrderHistoryResponse.ItemDetails();
            item.setFoodItemName(projection.getFoodName());
            item.setFoodItemPrice(projection.getFoodPrice());
            item.setQuantity(projection.getQuantity());
            item.setTotal(projection.getOrderTotal());

            orderMap.get(orderId).getItems().add(item);
        }

        return new ArrayList<>(orderMap.values());
    }

    public OrderHistoryResponse getOrderById(Long orderId) {
        return getOrderHistory().stream()
                .filter(o -> o.getOrderId().equals(String.valueOf(orderId)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }

    public List<OrderHistoryResponse> getOrdersOfCurrentEmployee(Long employeeId) {
        return getOrderHistory().stream()
                .filter(order -> order.getEmployeeId().equals(String.valueOf(employeeId)))
                .toList();
    }

    public List<OrderHistoryResponse> getOrdersOfCurrentEmployee(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<CurrentOrdersEmp> flatData = orderRepository.findOrdersByEmployeeAndDateRange(employeeId,startDate, endDate);

        Map<Long, OrderHistoryResponse> orderMap = new LinkedHashMap<>();

        for (CurrentOrdersEmp projection : flatData) {
            Long orderId = projection.getOrderId();

            orderMap.computeIfAbsent(orderId, id -> {
                OrderHistoryResponse order = new OrderHistoryResponse();
                order.setOrderId(String.valueOf(id));
                order.setCustomerName(projection.getCustomerName());
                order.setMobileNumber(projection.getMobileNumber());
                order.setEmployeeId(projection.getEmployeeId());
                order.setEmployeeName(projection.getEmployeeName());
                order.setOrderDate(projection.getOrderDate());
                order.setTotalAmount(projection.getOrderTotal());
                order.setItems(new ArrayList<>());
                return order;
            });

            OrderHistoryResponse.ItemDetails item = new OrderHistoryResponse.ItemDetails();
            item.setFoodItemName(projection.getFoodName());
            item.setFoodItemPrice(projection.getFoodPrice());
            item.setQuantity(projection.getQuantity());
            item.setTotal(projection.getItemTotal());

            orderMap.get(orderId).getItems().add(item);
        }

        return new ArrayList<>(orderMap.values());
    }


//        return orders.stream().map(order -> {
//            order.getId();
//            order.getEmployeeId();
//            order.getOrderDate();
//            order.getCustomerName();
//            order.getMobileNumber();
//            order.getTotalAmount();
//
//            return order;
//        }).toList();


}
