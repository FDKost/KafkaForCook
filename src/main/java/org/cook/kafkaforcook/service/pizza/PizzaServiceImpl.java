package org.cook.kafkaforcook.service.pizza;

import by.cook.core.ProductCartKafkaDTO;
import lombok.RequiredArgsConstructor;
import org.cook.kafkaforcook.entity.CookEntity;
import org.cook.kafkaforcook.entity.OrdersForCookingEntity;
import org.cook.kafkaforcook.entity.PizzaEntity;
import org.cook.kafkaforcook.repository.PizzaRepository;
import org.cook.kafkaforcook.service.ordersforcooking.OrdersForCookingService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PizzaServiceImpl implements PizzaService {

    private final PizzaRepository pizzaRepository;
    private final OrdersForCookingService ordersForCookingService;

    @Override
    public Optional<PizzaEntity> updatePizza(PizzaEntity pizzaEntity) {
        return pizzaRepository.findByName(pizzaEntity.getName())
                .map(pizzaRepository::saveAndFlush);
    }

    @Override
    public Optional<PizzaEntity> readPizza(String pizzaCode) {
        return pizzaRepository.findById(pizzaCode);
    }

    @Override
    public LocalTime getTimeToCompletePizza(ProductCartKafkaDTO productCartKafkaDTO, CookEntity cook) {
        Duration timeOfActualOrders = Duration.ZERO;
        Duration totalDuration = Duration.ZERO;
        float cookEff = cook.getExperienceCoefficient();

        Optional<PizzaEntity> pizzaEntity = pizzaRepository.findByName(productCartKafkaDTO.getProductName());

        if (pizzaEntity.isPresent()) {
            LocalTime pizzaTime = pizzaEntity.get().getTime();
            totalDuration = totalDuration.plus(Duration.ofHours(pizzaTime.getHour())
                    .plusMinutes(pizzaTime.getMinute())
                    .plusSeconds(pizzaTime.getSecond()));
            totalDuration = totalDuration.multipliedBy(productCartKafkaDTO.getQuantity());
            long seconds = totalDuration.getSeconds();
            long newSeconds = Math.round(seconds * cookEff);
            totalDuration = Duration.ofSeconds(newSeconds);
            if (ordersForCookingService.readAllByEmployeeId(cook.getEmployeeNumber()) != null) {
                List<OrdersForCookingEntity> orders = ordersForCookingService.readAllByEmployeeId(cook.getEmployeeNumber());
                if (!orders.isEmpty()) {
                    for (OrdersForCookingEntity ordersForCookingEntity : orders) {
                        timeOfActualOrders.plusHours(ordersForCookingEntity.getEstimatedCompletionTime().getHour())
                                .plusMinutes(ordersForCookingEntity.getEstimatedCompletionTime().getMinute())
                                .plusSeconds(ordersForCookingEntity.getEstimatedCompletionTime().getSecond());
                    }
                }
            }
        }
        LocalTime timeForOrder = LocalTime.now().plus(totalDuration);
        timeForOrder = timeForOrder.plus(timeOfActualOrders);
        return timeForOrder;
    }
}
