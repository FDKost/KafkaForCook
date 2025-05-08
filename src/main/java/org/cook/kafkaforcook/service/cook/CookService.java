package org.cook.kafkaforcook.service.cook;

import by.cook.core.ProductCartKafkaDTO;
import org.cook.kafkaforcook.dto.CookToProcessOrderDTO;
import org.cook.kafkaforcook.entity.CookEntity;
import org.cook.kafkaforcook.entity.PizzaEntity;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CookService {
    CookEntity create(CookEntity cook);

    Optional<CookEntity> read(UUID id);

    List<CookEntity> readAll();

    Optional<CookEntity> update(UUID id, CookEntity cook);

    void delete(UUID id);

    CookToProcessOrderDTO fastestCookToProcessOrder(List<CookEntity> cooks, ProductCartKafkaDTO dto);

    LocalTime getTimeToCompletePizza(Optional<PizzaEntity> pizza,
                                     ProductCartKafkaDTO productCartKafkaDTO, CookEntity cook);

}
