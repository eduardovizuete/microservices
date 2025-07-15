package com.codearqui.serviceorder.services;

import com.codearqui.serviceorder.dto.OrderRequest;
import com.codearqui.serviceorder.dto.OrderResponse;
import com.codearqui.serviceorder.model.entity.OrderModel;
import com.codearqui.serviceorder.model.mapper.OrderMapper;
import com.codearqui.serviceorder.proxy.api.InventoryApi;
import com.codearqui.serviceorder.proxy.model.InventoryResponse;
import com.codearqui.serviceorder.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.reactor.timelimiter.TimeLimiterOperator;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
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
                .map(OrderMapper.INSTANCE::modelToRequest);
    }

    @Override
    public Mono<OrderResponse> getOrderById(int orderId) {
        return orderRepository.findById(orderId)
                .map(OrderMapper.INSTANCE::modelToRequest);
    }

    @Override
    public Flux<OrderResponse> getList() {
        return orderRepository.findAll()
                .map(OrderMapper.INSTANCE::modelToRequest);
    }

    @Override
    public Mono<OrderResponse> updateOrder(int orderId) {
        return orderRepository.findById(orderId)
                .map(value -> {
                    value.setStatusOrder(OrderResponse.StatusEnum.COMPLETED.getValue());
                    return value;
                })
                .flatMap(value -> invokeServices(value)
                        .map(x -> value)
                        .switchIfEmpty(Mono.empty())
                ) ///  Invocación al servicio de inventario
                .flatMap(orderRepository::save)
                .map(OrderMapper.INSTANCE::modelToRequest)
                .doOnSubscribe(value -> log.info("Updating order with id: {}", orderId))
                .doOnSuccess(value -> log.info("Order updated successfully with id: {}", orderId))
                .doOnError(throwable -> log.error("Error update: {}", orderId, throwable));
    }

    private Mono<InventoryResponse> invokeServices(OrderModel value) {
        return inventoryApi.getInventory(value.getCodeProduct())
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .transformDeferred(RetryOperator.of(retry))
                .transformDeferred(RateLimiterOperator.of(rateLimiter))
                .transformDeferred(TimeLimiterOperator.of(timeLimiter))
                .onErrorResume(CallNotPermittedException.class, e -> fallBack());
    }

    private Mono<InventoryResponse> fallBack() {
        return Mono.error(new RuntimeException("Circuit Breaker is open"));
    }
}
