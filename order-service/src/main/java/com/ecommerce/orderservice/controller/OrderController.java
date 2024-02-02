package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.client.InventoryClient;
import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.event.OrderPlacedEvent;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping
    public String placeOrder(@RequestBody OrderDto orderDto) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("inventory");
        Supplier<Boolean> booleanSupplier = () -> orderDto.getOrderLineItems().stream()
                .allMatch(orderLineItem -> {
                    log.info("Calling Inventory Service for skuCode: " + orderLineItem.getSkuCode());
                    return inventoryClient.checkStock(orderLineItem.getSkuCode());
                });
        boolean productsInStock = circuitBreaker.run(booleanSupplier, throwable -> handleErrorCase());

        if (productsInStock) {
            Order order = new Order();
            order.setOrderLineItems(orderDto.getOrderLineItems());
            order.setOrderNumber(UUID.randomUUID().toString());
            orderRepository.save(order);

            log.info("Sending order details to notification-service for order number: " + order.getId());
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "Order placed successfully";
        } else {
            return "Order failed, one of the products is out of stock";
        }
    }

    private Boolean handleErrorCase() {
        return false;
    }
}
