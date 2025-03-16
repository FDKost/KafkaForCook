package org.cook.kafkaforcook.service.ordersforcooking;

import lombok.RequiredArgsConstructor;
import org.cook.kafkaforcook.entity.OrdersForCookingEntity;
import org.cook.kafkaforcook.repository.OrdersForCookingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdersForCookingServiceImpl implements OrdersForCookingService {
    private final OrdersForCookingRepository ordersForCookingRepository;

    @Override
    public OrdersForCookingEntity create(OrdersForCookingEntity ordersForCookingEntity) {
        return ordersForCookingRepository.save(ordersForCookingEntity);
    }

    @Override
    public Optional<OrdersForCookingEntity> readByEmployeeId(UUID employeeNumber) {
        return ordersForCookingRepository.findByEmployeeId(employeeNumber);
    }

    @Override
    public Optional<OrdersForCookingEntity> readByOrderId(UUID orderId) {
        return ordersForCookingRepository.findByOrderId(orderId);
    }

    @Override
    public Optional<OrdersForCookingEntity> update(OrdersForCookingEntity ordersForCookingEntity) {
        return ordersForCookingRepository.findByOrderId(ordersForCookingEntity.getOrderNumber())
                .map(ordersForCookingRepository::saveAndFlush);
    }

    @Override
    public void delete(OrdersForCookingEntity ordersForCookingEntity) {
        ordersForCookingRepository.findByOrderId(ordersForCookingEntity.getOrderNumber())
                .ifPresent(ordersForCookingRepository::delete);
    }
}
