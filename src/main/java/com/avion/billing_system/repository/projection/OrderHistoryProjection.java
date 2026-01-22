package com.avion.billing_system.repository.projection;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderHistoryProjection {
    Long getOrderId();
    String getCustomerName();
    String getMobileNumber();
    String getEmployeeId();
    String getEmployeeName();
    LocalDateTime getOrderDate();
    Double getOrderTotal();

    String getFoodName();
    Double getFoodPrice();
    Integer getQuantity();
    Double getItemTotal();


}
