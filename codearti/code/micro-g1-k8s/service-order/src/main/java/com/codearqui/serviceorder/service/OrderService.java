package com.codearqui.serviceorder.service;

import com.codearqui.serviceorder.dto.OrderRequest;
import com.codearqui.serviceorder.dto.OrderResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<OrderResponse> createOrder(OrderRequest orderRequest);
    Mono<OrderResponse> getOrderById(int orderId);
    Flux<OrderResponse> getList();
    Mono<OrderResponse> updateOrder(int OrderId);
}
