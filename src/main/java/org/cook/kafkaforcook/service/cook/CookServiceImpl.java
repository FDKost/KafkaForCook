package org.cook.kafkaforcook.service.cook;

import by.cook.core.ProductCartKafkaDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cook.kafkaforcook.dto.CookToProcessOrderDTO;
import org.cook.kafkaforcook.entity.CookEntity;
import org.cook.kafkaforcook.entity.OrdersForCookingEntity;
import org.cook.kafkaforcook.entity.PizzaEntity;
import org.cook.kafkaforcook.repository.CookRepository;
import org.cook.kafkaforcook.repository.PizzaRepository;
import org.cook.kafkaforcook.service.ordersforcooking.OrdersForCookingService;
import org.cook.kafkaforcook.util.QuickSort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class CookServiceImpl implements CookService {
    private final CookRepository cookRepository;
    private final PizzaRepository pizzaRepository;
    private final OrdersForCookingService ordersForCookingService;

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
    public LocalTime getTimeToCompletePizza(Optional<PizzaEntity> pizza,
                                            ProductCartKafkaDTO productCartKafkaDTO, CookEntity cook) {
        Duration timeOfActualOrders = Duration.ZERO;
        Duration totalDuration = Duration.ZERO;
        float cookEff = cook.getExperienceCoefficient();

        if (pizza.isPresent()) {
            LocalTime pizzaTime = pizza.get().getTime();
            totalDuration = totalDuration.plus(Duration.ofHours(pizzaTime.getHour())
                    .plusMinutes(pizzaTime.getMinute())
                    .plusSeconds(pizzaTime.getSecond()));
            totalDuration = totalDuration.multipliedBy(productCartKafkaDTO.getQuantity());
            long seconds = totalDuration.getSeconds();
            long newSeconds = Math.round(seconds * cookEff);
            totalDuration = Duration.ofSeconds(newSeconds);
            if (ordersForCookingService.readAllByEmployeeId(cook.getEmployeeNumber()) != null) {
                List<OrdersForCookingEntity> orders = ordersForCookingService
                        .readAllByEmployeeId(cook.getEmployeeNumber());
                if (!orders.isEmpty()) {
                    for (OrdersForCookingEntity ordersForCookingEntity : orders) {
                        timeOfActualOrders = timeOfActualOrders.plusHours(ordersForCookingEntity.getEstimatedCompletionTime().getHour())
                                .plusMinutes(ordersForCookingEntity.getEstimatedCompletionTime().getMinute())
                                .plusSeconds(ordersForCookingEntity.getEstimatedCompletionTime().getSecond());
                    }
                }
            }
        }
        LocalTime timeForOrder = LocalTime.now().plus(totalDuration);
        timeForOrder = timeForOrder.plus(timeOfActualOrders);
        LocalDateTime timeToSend = LocalDateTime.of(LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth(),
                timeForOrder.getHour(), timeForOrder.getMinute(),
                timeForOrder.getSecond());
        return timeToSend.toLocalTime();
    }

    @Override
    @SneakyThrows
    public CookToProcessOrderDTO fastestCookToProcessOrder(List<CookEntity> cooks, ProductCartKafkaDTO dto) {
        CookToProcessOrderDTO chosenCook = new CookToProcessOrderDTO();
        List<Integer> timeList = new ArrayList<>();
        QuickSort qs = new QuickSort();
        LocalTime time = LocalTime.MAX;
        CookEntity fastestCook = null;
        int count = cooks.size();
        Optional<PizzaEntity> pizzaToProcess = pizzaRepository.findByName(dto.getProductName());
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            CountDownLatch latch = new CountDownLatch(count);
            HashMap<CookEntity, LocalTime> cookMap = new HashMap<>();
            for (CookEntity cook : cooks) {
                executor.submit(() -> {
                    try {
                        if (cook.getStartWorkTime().isBefore(LocalTime.now()) &&
                                cook.getEndWorkTime().isAfter(LocalTime.now())) {
                            cookMap.put(cook, getTimeToCompletePizza(pizzaToProcess, dto, cook));
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
                timeList.add(entry.getValue().toSecondOfDay());
            }
            Map<CookEntity, LocalTime> returnMap = qs.quickSortAndReturnFirstFromHashMap(cookMap);
            for (Map.Entry<CookEntity, LocalTime> entry : returnMap.entrySet()) {
                if (entry.getValue().equals(cookMap.get(entry.getKey()))) {
                    fastestCook = entry.getKey();
                    time = entry.getValue();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        chosenCook = chosenCook.parseDTO(fastestCook, time);
        return chosenCook;
    }
}
