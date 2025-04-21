package org.cook.kafkaforcook.service.ordersforcooking;

import by.cook.core.ProductCartKafkaDTO;
import org.cook.kafkaforcook.entity.CookEntity;
import org.cook.kafkaforcook.entity.OrdersForCookingEntity;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrdersForCookingService {
    OrdersForCookingEntity create(OrdersForCookingEntity ordersForCookingEntity);

    Optional<OrdersForCookingEntity> readByEmployeeId(UUID employeeNumber);

    List<OrdersForCookingEntity> readAllByEmployeeId(UUID employeeId);

    Optional<OrdersForCookingEntity> readByOrderId(UUID orderId);

    Optional<OrdersForCookingEntity> update(OrdersForCookingEntity ordersForCookingEntity);

    void delete(OrdersForCookingEntity ordersForCookingEntity);

    void checkOrdersComplete();

    OrdersForCookingEntity parseDto(ProductCartKafkaDTO dto, CookEntity entity, LocalTime timeToComplete);
}
