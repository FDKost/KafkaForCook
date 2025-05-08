package org.cook.kafkaforcook.service.ordersforcooking;

import by.cook.core.ProductCartKafkaDTO;
import lombok.RequiredArgsConstructor;
import org.cook.kafkaforcook.entity.CookEntity;
import org.cook.kafkaforcook.entity.OrdersForCookingEntity;
import org.cook.kafkaforcook.repository.OrdersForCookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrdersForCookingServiceImpl implements OrdersForCookingService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final OrdersForCookingRepository ordersForCookingRepository;

    @Override
    public OrdersForCookingEntity create(OrdersForCookingEntity ordersForCookingEntity) {
        return ordersForCookingRepository.save(ordersForCookingEntity);
    }

    @Override
    public Optional<OrdersForCookingEntity> readByEmployeeId(UUID employeeNumber) {
        return ordersForCookingRepository.findByEmployeeEmployeeNumber(employeeNumber);
    }

    @Override
    public List<OrdersForCookingEntity> readAllByEmployeeId(UUID employeeId) {
        return ordersForCookingRepository.findAllByEmployeeEmployeeNumber(employeeId);
    }

    @Override
    public Optional<OrdersForCookingEntity> readByOrderId(UUID orderId) {
        return ordersForCookingRepository.findByOrderNumber(orderId);
    }

    @Override
    public Optional<OrdersForCookingEntity> update(OrdersForCookingEntity ordersForCookingEntity) {
        return ordersForCookingRepository.findByOrderNumber(ordersForCookingEntity.getOrderNumber())
                .map(ordersForCookingRepository::saveAndFlush);
    }

    @Override
    public void delete(OrdersForCookingEntity ordersForCookingEntity) {
        ordersForCookingRepository.findByOrderNumber(ordersForCookingEntity.getOrderNumber())
                .ifPresent(ordersForCookingRepository::delete);
    }

    private void mergeOrdersBeforeSend(OrdersForCookingEntity order) {
        List<OrdersForCookingEntity> ordersForCookingEntities = ordersForCookingRepository.findAll();
        List<OrdersForCookingEntity> ordersToMerge = new ArrayList<>();
        OrdersForCookingEntity entityToSend = null;
        for (OrdersForCookingEntity ordersForCookingEntity : ordersForCookingEntities) {
            if (ordersForCookingEntity.getCartId().equals(order.getCartId())) {
                ordersToMerge.add(ordersForCookingEntity);
            }
        }
        if (ordersToMerge.size() > 1) {
            LocalTime latestTime = LocalTime.MIDNIGHT;
            for (OrdersForCookingEntity orderForMergePreparationEntity : ordersToMerge) {
                if (entityToSend == null) {
                    entityToSend = orderForMergePreparationEntity;
                }
                if (orderForMergePreparationEntity != entityToSend) {
                    entityToSend.setPizzaList(entityToSend.getPizzaList() + ", "
                            + orderForMergePreparationEntity.getPizzaList());
                    if (orderForMergePreparationEntity.getEstimatedCompletionTime().isAfter(latestTime)) {
                        latestTime = orderForMergePreparationEntity.getEstimatedCompletionTime();
                    }
                }
            }
            entityToSend.setEstimatedCompletionTime(latestTime);
            //method to send
        } else {
            entityToSend = ordersToMerge.get(0);
            //method to send
        }
    }

    private boolean checkAllPizzasInOrderIsDone(OrdersForCookingEntity order) {
        List<OrdersForCookingEntity> orders = ordersForCookingRepository.findAllByCartId(order.getCartId());
        boolean result = true;
        for (OrdersForCookingEntity orderForCookingEntity : orders) {
            if (LocalTime.now().isBefore(orderForCookingEntity.getEstimatedCompletionTime())) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public void checkOrdersComplete() {
        List<OrdersForCookingEntity> orders = ordersForCookingRepository.findAll();
        List<OrdersForCookingEntity> ordersToDelete = new ArrayList<>();
        Map<OrdersForCookingEntity, String> ordersToMerge = new HashMap<>();
        for (OrdersForCookingEntity order : orders) {
            if (checkAllPizzasInOrderIsDone(order)) {
                ordersToMerge.put(order, String.valueOf(order.getCartId()));
                ordersToDelete.add(order);
            }
        }
        if (!ordersToDelete.isEmpty()) {
            LOGGER.info("Have completed orders!");
            for (Map.Entry<OrdersForCookingEntity, String> entry : ordersToMerge.entrySet()) {
                mergeOrdersBeforeSend(entry.getKey());
            }
            ordersForCookingRepository.deleteAll(ordersToDelete);
            LOGGER.info("Orders merged and ready to send!");
        } else {
            LOGGER.info("No orders have been completed!");
        }

    }

    @Override
    public OrdersForCookingEntity parseDto(ProductCartKafkaDTO dto, CookEntity entity, LocalTime timeToComplete) {
        OrdersForCookingEntity order = new OrdersForCookingEntity();
        order.setEmployee(entity);
        order.setCartId(dto.getCartId());
        order.setEstimatedCompletionTime(timeToComplete);
        order.setPizzaList(dto.getQuantity() + " " + dto.getProductName());
        return order;
    }
}
