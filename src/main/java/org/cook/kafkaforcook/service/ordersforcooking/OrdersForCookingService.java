package org.cook.kafkaforcook.service.ordersforcooking;

import org.cook.kafkaforcook.entity.OrdersForCookingEntity;

import java.util.Optional;
import java.util.UUID;

public interface OrdersForCookingService {
    OrdersForCookingEntity create(OrdersForCookingEntity ordersForCookingEntity);
    Optional<OrdersForCookingEntity> readByEmployeeId(UUID employeeNumber);
    Optional<OrdersForCookingEntity> readByOrderId(UUID orderId);
    Optional<OrdersForCookingEntity> update(OrdersForCookingEntity ordersForCookingEntity);
    void delete(OrdersForCookingEntity ordersForCookingEntity);
}
