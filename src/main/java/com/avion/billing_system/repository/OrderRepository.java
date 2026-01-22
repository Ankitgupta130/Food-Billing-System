package com.avion.billing_system.repository;

import com.avion.billing_system.dto.OrderHistoryResponse;
import com.avion.billing_system.entity.Order;
import com.avion.billing_system.repository.projection.CurrentOrdersEmp;
import com.avion.billing_system.repository.projection.OrderHistoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    String DATAQUERY  = """
            SELECT
            o.id as orderId,
            o.customer_name as customerName,
            o.mobile_number as mobileNumber,
            o.employee_id as employeeId,
            e.name as employeeName,
            o.order_date as orderDate,
            o.total_amount as orderTotal,
            f.name as foodName,
            f.price as foodPrice,
            od.quantity as quantity,
            od.total as itemTotal
            FROM orders o
            JOIN order_details od ON o.id = od.order_id
            JOIN food_items f ON f.id = od.food_item_id
            JOIN employees e ON o.employee_id = e.id
            ORDER BY o.id DESC
            """;

    @Query( value = DATAQUERY, nativeQuery = true)
    List<OrderHistoryProjection> getOrderHistory();


    String DATEQUERY = """
                    SELECT
                        o.id as orderId,
                        o.customer_name as customerName,
                        o.mobile_number as mobileNumber,
                        o.employee_id as employeeId,
                        e.name as employeeName,
                        o.order_date as orderDate,
                        o.total_amount as orderTotal,
                        f.name as foodName,
                        f.price as foodPrice,
                        od.quantity as quantity,
                        od.total as itemTotal
                    FROM orders o
                    JOIN order_details od ON o.id = od.order_id
                    JOIN food_items f ON f.id = od.food_item_id
                    JOIN employees e ON o.employee_id = e.id
                    WHERE o.order_date BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE) + INTERVAL '1 day'
                    ORDER BY o.id DESC
            """;

    @Query (value = DATEQUERY, nativeQuery = true)
    List<OrderHistoryProjection> getOrderHistoryBetweenDates(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate


    );

    @Query(value = """
            SELECT
            e.name AS employeeName,
            e.id AS employeeId,
            o.id AS orderId,
            o.customer_name as customerName,
            o.mobile_number as mobileNumber,
            o.order_date AS orderDate,
            o.total_amount AS orderTotal,
            f.name AS foodName,
            f.price AS foodPrice,
            od.quantity AS quantity,
            od.total AS itemTotal
            FROM orders o
            JOIN order_details od ON o.id = od.order_id
            JOIN food_items f ON f.id = od.food_item_id
            JOIN employees e ON o.employee_id = e.id
            WHERE o.employee_id = :employeeId
            AND o.order_date BETWEEN CAST(:startDate AS DATE)
            AND (CAST(:endDate AS DATE) + INTERVAL '1 day')
            ORDER BY o.id DESC, o.order_date DESC;
            """, nativeQuery = true)
    List<CurrentOrdersEmp> findOrdersByEmployeeAndDateRange(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
