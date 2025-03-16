package org.cook.kafkaforcook.service.pizza;

import lombok.RequiredArgsConstructor;
import org.cook.kafkaforcook.entity.PizzaEntity;
import org.cook.kafkaforcook.repository.PizzaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PizzaServiceImpl implements PizzaService {

    private final PizzaRepository pizzaRepository;

    @Override
    public Optional<PizzaEntity> updatePizza(PizzaEntity pizzaEntity) {
        return pizzaRepository.findByName(pizzaEntity.getName())
                .map(pizzaRepository::saveAndFlush);
    }

    @Override
    public Optional<PizzaEntity> readPizza(String pizzaCode) {
        return pizzaRepository.findById(pizzaCode);
    }
}
