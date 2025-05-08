package org.cook.kafkaforcook.service.handler;

import by.cook.core.ProductCartKafkaDTO;
import lombok.RequiredArgsConstructor;
import org.cook.kafkaforcook.dto.CookToProcessOrderDTO;
import org.cook.kafkaforcook.entity.OrdersForCookingEntity;
import org.cook.kafkaforcook.service.cook.CookService;
import org.cook.kafkaforcook.service.ordersforcooking.OrdersForCookingService;
import org.cook.kafkaforcook.service.pizza.PizzaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventHandlerServiceImpl implements EventHandlerService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final CookService cookService;
    private final OrdersForCookingService ordersForCookingService;
    private final PizzaService pizzaService;

    @Override
    public void handleEvent(ProductCartKafkaDTO dto) {
        LOGGER.info("Received event: {}", dto.getProductName());
        try {
            CookToProcessOrderDTO cookToCookOrder = cookService.fastestCookToProcessOrder(cookService.readAll(), dto);
            OrdersForCookingEntity entity = ordersForCookingService.parseDto(dto, cookToCookOrder.getCook(), cookToCookOrder.getEstimatedCompletionTime());
            ordersForCookingService.create(entity);
            LOGGER.info("Created order for: {}", dto.getProductName());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
