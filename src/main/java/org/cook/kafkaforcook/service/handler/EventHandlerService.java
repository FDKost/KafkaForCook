package org.cook.kafkaforcook.service.handler;

import by.cook.core.ProductCartKafkaDTO;

public interface EventHandlerService {
    void handleEvent(ProductCartKafkaDTO dto);
}
