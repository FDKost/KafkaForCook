package org.cook.kafkaforcook.service.pizza;

import org.cook.kafkaforcook.entity.PizzaEntity;

import java.util.Optional;

public interface PizzaService {
    Optional<PizzaEntity> updatePizza(PizzaEntity pizzaEntity);

    Optional<PizzaEntity> readPizza(String pizzaCode);

}
