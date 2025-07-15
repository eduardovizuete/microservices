package com.codearqui.serviceinventory.expose;

import com.codearqui.serviceinventory.api.InventoryApiDelegate;
import com.codearqui.serviceinventory.dto.InventoryRequest;
import com.codearqui.serviceinventory.dto.InventoryResponse;
import com.codearqui.serviceinventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryApiImpl implements InventoryApiDelegate {
    private final InventoryService inventoryService;

    @Override
    public Mono<InventoryResponse> getInventory(String productId,
                                                ServerWebExchange exchange) {
        return inventoryService.getInventory(productId);
    }

    @Override
    public Flux<InventoryResponse> listInventory(ServerWebExchange exchange) {
        return inventoryService.getList();
                //.delayElements(java.time.Duration.ofSeconds(5));
    }

    @Override
    public Mono<InventoryResponse> registerInventory(Mono<InventoryRequest> inventoryRequest,
                                                      ServerWebExchange exchange) {
        return inventoryRequest.flatMap(inventoryService::createOrder);
    }
}
