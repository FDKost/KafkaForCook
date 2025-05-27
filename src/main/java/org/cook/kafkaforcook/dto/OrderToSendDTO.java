package org.cook.kafkaforcook.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderToSendDTO {
    private UUID cartId;
    private String pizzaList;
}
