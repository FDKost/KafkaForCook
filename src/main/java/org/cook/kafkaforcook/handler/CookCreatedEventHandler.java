package org.cook.kafkaforcook.handler;

import by.cook.core.ProductCartKafkaDTO;
import lombok.RequiredArgsConstructor;
import org.cook.kafkaforcook.service.handler.EventHandlerService;
import org.cook.kafkaforcook.service.ordersforcooking.OrdersForCookingService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@KafkaListener(topics = "cook-created-events-topic")
public class CookCreatedEventHandler {
    private final EventHandlerService eventHandlerService;
    private final OrdersForCookingService ordersForCookingService;

    @KafkaHandler
    public void handle(ProductCartKafkaDTO dto) {
        eventHandlerService.handleEvent(dto);
    }

    @Scheduled(fixedDelay = 60000)
    public void scheduled() {
        ordersForCookingService.checkOrdersComplete();
    }
}
