package org.cook.kafkaforcook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders_for_cooking")
@Entity
public class OrdersForCookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_number")
    private UUID orderNumber;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_number")
    private CookEntity employee;
    @Column(name = "cart_id")
    private UUID cartId;
    @Column(name = "estimated_completion_time")
    private LocalTime estimatedCompletionTime;
    @Column(name = "pizza_list")
    private String pizzaList;
}
