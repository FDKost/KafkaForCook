package org.cook.kafkaforcook.handler;

import by.cook.core.ProductCartKafkaDTO;
import lombok.RequiredArgsConstructor;
import org.cook.kafkaforcook.entity.CookEntity;
import org.cook.kafkaforcook.entity.OrdersForCookingEntity;
import org.cook.kafkaforcook.service.cook.CookService;
import org.cook.kafkaforcook.service.ordersforcooking.OrdersForCookingService;
import org.cook.kafkaforcook.service.pizza.PizzaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@KafkaListener(topics = "cook-created-events-topic")
public class CookCreatedEventHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final CookService cookService;
    private final OrdersForCookingService ordersForCookingService;
    private final PizzaService pizzaService;

    @KafkaHandler
    public void handle(ProductCartKafkaDTO dto) {
        LOGGER.info("Received event: {}", dto.getProductName());
        try {
            CookEntity cookToCookOrder = cookService.fastestCookToProcessOrder(cookService.readAll(), dto);
            LocalTime timeToComplete = pizzaService.getTimeToCompletePizza(dto, cookToCookOrder);
            OrdersForCookingEntity entity = ordersForCookingService.parseDto(dto, cookToCookOrder, timeToComplete);
            ordersForCookingService.create(entity);
            LOGGER.info("Created order for: {}", dto.getProductName());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void scheduled() {
        ordersForCookingService.checkOrdersComplete();
    }
}
