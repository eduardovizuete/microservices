package com.codearqui.serviceorder.expose;

import com.codearqui.serviceorder.api.OrdenesApiDelegate;
import com.codearqui.serviceorder.dto.OrderRequest;
import com.codearqui.serviceorder.dto.OrderResponse;
import com.codearqui.serviceorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAppiImpl implements OrdenesApiDelegate {

    private final OrderService orderService;

    @Override
    public Mono<OrderResponse> createOrder(Mono<OrderRequest> orderRequest, ServerWebExchange exchange) {
        return orderRequest.flatMap(orderService::createOrder)
                .doOnSubscribe(value -> log.info("Creating Order"))
                .doOnSuccess(value -> log.info("Order created successfully"))
                .doOnError(throwable -> log.error("Error creating order", throwable));
    }

    @Override
    public Mono<OrderResponse> getOrder(Integer orderId, ServerWebExchange exchange) {
        return orderService.getOrderById(orderId)
                .doOnSuccess(value -> {
                    if (Optional.ofNullable(value).isEmpty()) {
                        exchange.getResponse().setStatusCode(HttpStatus.NO_CONTENT);
                    }
                })
                .doOnSubscribe(value -> log.info("Getting order with id: {}", orderId))
                .doOnError(throwable -> log.error("Error getting order", throwable));
    }

    @Override
    public Flux<OrderResponse> listOrders(ServerWebExchange exchange) {
        return  orderService.getList();
    }

    @Override
    public Mono<Void> updateOrder(Integer orderId, ServerWebExchange exchange) {
        return orderService.updateOrder(orderId)
                .doOnSuccess(value -> {
                    if (Optional.ofNullable(value).isEmpty()) {
                        exchange.getResponse().setStatusCode(HttpStatus.NO_CONTENT);
                    }
                })
                .then();
    }

}
