package com.codearqui.serviceorder.service.impl;

import com.codearqui.serviceorder.dto.OrderRequest;
import com.codearqui.serviceorder.dto.OrderResponse;
import com.codearqui.serviceorder.model.entity.OrderModel;
import com.codearqui.serviceorder.model.mapper.OrderMapper;
import com.codearqui.serviceorder.proxy.api.InventoryApi;
import com.codearqui.serviceorder.proxy.model.InventoryResponse;
import com.codearqui.serviceorder.repository.OrderRepository;
import com.codearqui.serviceorder.service.OrderService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.reactor.timelimiter.TimeLimiterOperator;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryApi inventoryApi;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;
    private final RateLimiter rateLimiter;
    private final TimeLimiter timeLimiter;

    @Override
    public Mono<OrderResponse> createOrder(OrderRequest orderRequest) {
        var orderDto = OrderMapper.INSTANCE.requestToModel(orderRequest);
        orderDto.setDateOrder(LocalDateTime.now().toString());
        orderDto.setStatusOrder(OrderResponse.StatusEnum.PENDING.getValue());

        return orderRepository.save(orderDto)
                .map(OrderMapper.INSTANCE::modelToResponse);
    }

    @Override
    public Mono<OrderResponse> getOrderById(int orderId) {
        return orderRepository.findById(orderId)
                .map(OrderMapper.INSTANCE::modelToResponse);
    }

    @Override
    public Flux<OrderResponse> getList() {
        return orderRepository.findAll()
                .map(OrderMapper.INSTANCE::modelToResponse);
    }

    @Override
    public Mono<OrderResponse> updateOrder(int orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatusOrder(OrderResponse.StatusEnum.COMPLETED.getValue());
                    return order;
                })
                .flatMap(order -> invokeServices(order)
                        .map(x -> order)
                        .switchIfEmpty(Mono.empty())
                ) // invocacion al servicio de inventario
                .flatMap(orderRepository::save)
                .map(OrderMapper.INSTANCE::modelToResponse)
                .doOnSubscribe(order -> log.info("Updating order with id {}", orderId))
                .doOnSuccess(order -> log.info("Order updated successfully with id {}", orderId))
                .doOnError(throwable -> log.error("Error update order with id {}", orderId, throwable));
    }

    private Mono<InventoryResponse> invokeServices(OrderModel order) {
        return inventoryApi.getInventory(order.getCodeProduct())
                //.transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                //.transformDeferred(RetryOperator.of(retry))
                //.transformDeferred(RateLimiterOperator.of(rateLimiter))
                .transformDeferred(TimeLimiterOperator.of(timeLimiter))
                .onErrorResume(CallNotPermittedException.class, e -> fallBack());
    }

    private Mono<InventoryResponse> fallBack() {
        return Mono.error(new RuntimeException("Circuit breaker is open"));
    }

}
