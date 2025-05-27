package org.cook.kafkaforcook.service.delivery;

import org.cook.kafkaforcook.dto.OrderToSendDTO;

public interface SendProductsToDeliveryService {
    String sendProductsToDelivery(OrderToSendDTO dto);
}
