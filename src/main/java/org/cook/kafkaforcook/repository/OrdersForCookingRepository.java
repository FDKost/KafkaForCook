package org.cook.kafkaforcook.repository;

import org.cook.kafkaforcook.entity.OrdersForCookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdersForCookingRepository extends JpaRepository<OrdersForCookingEntity, UUID> {
    Optional<OrdersForCookingEntity> findByOrderId(UUID orderId);
    List<OrdersForCookingEntity> findAllByEmployeeId(UUID employeeId);
    Optional<OrdersForCookingEntity> findByEmployeeId(UUID employeeId);
}
