package org.cook.kafkaforcook.service.cook;

import by.cook.core.ProductCartKafkaDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cook.kafkaforcook.entity.CookEntity;
import org.cook.kafkaforcook.repository.CookRepository;
import org.cook.kafkaforcook.service.pizza.PizzaService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class CookServiceImpl implements CookService {
    private final CookRepository cookRepository;
    private final PizzaService pizzaService;

    @Override
    public CookEntity create(CookEntity cook) {
        return cookRepository.save(cook);
    }

    @Override
    public Optional<CookEntity> read(UUID id) {
        return cookRepository.findById(id);
    }

    @Override
    public List<CookEntity> readAll() {
        return cookRepository.findAll();
    }

    @Override
    public Optional<CookEntity> update(UUID id, CookEntity cook) {
        return cookRepository.findById(id)
                .map(cookRepository::saveAndFlush);
    }

    @Override
    public void delete(UUID id) {
        cookRepository.findById(id)
                .ifPresent(cookRepository::delete);
    }

    @Override
    @SneakyThrows
    public CookEntity fastestCookToProcessOrder(List<CookEntity> cooks, ProductCartKafkaDTO dto) {
        ExecutorService executor = Executors.newCachedThreadPool();
        LocalTime time = LocalTime.MAX;
        LocalTime timeNow = LocalTime.now();
        CookEntity fastestCook = null;
        int count = cooks.size();
        CountDownLatch latch = new CountDownLatch(count);
        HashMap<CookEntity, LocalTime> cookMap = new HashMap<>();
        for (CookEntity cook : cooks) {
            executor.submit(() -> {
                try {
                    if (cook.getStartWorkTime().isBefore(timeNow) &&
                            cook.getEndWorkTime().isAfter(timeNow)) {
                        cookMap.put(cook, pizzaService.getTimeToCompletePizza(dto, cook));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();
        for (Map.Entry<CookEntity, LocalTime> entry : cookMap.entrySet()) {
            if (entry.getValue().isBefore(time)) {
                time = entry.getValue();
                fastestCook = entry.getKey();
            }
        }

        return fastestCook;
    }
}
