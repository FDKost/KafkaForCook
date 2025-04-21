package org.cook.kafkaforcook.repository;

import org.cook.kafkaforcook.entity.OrdersForCookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdersForCookingRepository extends JpaRepository<OrdersForCookingEntity, UUID> {
    Optional<OrdersForCookingEntity> findByOrderNumber(UUID orderId);

    Optional<OrdersForCookingEntity> findByEmployeeEmployeeNumber(UUID employeeId);

    List<OrdersForCookingEntity> findAllByCartId(UUID cartId);

    List<OrdersForCookingEntity> findAllByEmployeeEmployeeNumber(UUID employeeId);
}
