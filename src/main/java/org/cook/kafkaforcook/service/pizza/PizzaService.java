package org.cook.kafkaforcook.service.pizza;

import by.cook.core.ProductCartKafkaDTO;
import org.cook.kafkaforcook.entity.CookEntity;
import org.cook.kafkaforcook.entity.PizzaEntity;

import java.time.LocalTime;
import java.util.Optional;

public interface PizzaService {
    Optional<PizzaEntity> updatePizza(PizzaEntity pizzaEntity);

    Optional<PizzaEntity> readPizza(String pizzaCode);

    LocalTime getTimeToCompletePizza(ProductCartKafkaDTO productCartKafkaDTOS, CookEntity cook);
}
