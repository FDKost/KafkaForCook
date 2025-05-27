package org.cook.kafkaforcook.service.delivery;

import lombok.RequiredArgsConstructor;
import org.cook.kafkaforcook.dto.OrderToSendDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendProductsToDeliveryServiceImpl implements SendProductsToDeliveryService {
    private final KafkaTemplate<String, OrderToSendDTO> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public String sendProductsToDelivery(OrderToSendDTO dto) {
        kafkaTemplate.send("delivery-created-events-topic", dto.getCartId().toString(), dto);
        LOGGER.info("Sending to delivery order with id: {}", dto.getCartId().toString());
        return dto.getCartId().toString();
    }
}
