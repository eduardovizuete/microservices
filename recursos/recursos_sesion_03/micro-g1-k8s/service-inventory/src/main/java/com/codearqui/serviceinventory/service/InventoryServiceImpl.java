package com.codearqui.serviceinventory.service;

import com.codearqui.serviceinventory.dto.InventoryRequest;
import com.codearqui.serviceinventory.dto.InventoryResponse;
import com.codearqui.serviceinventory.model.mapper.InventoryMapper;
import com.codearqui.serviceinventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventory;

    @Override
    public Mono<InventoryResponse> createOrder(InventoryRequest inventoryRequest) {
        var dtoRequest = InventoryMapper.INSTANCE.requestToModel(inventoryRequest);
        return inventory.existsByCode(dtoRequest.getCode())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("The product already exists"));
                    }
                    return inventory.save(dtoRequest)
                            .map(InventoryMapper.INSTANCE::modelToResponse);
                });
    }

    @Override
    public Mono<InventoryResponse> getInventory(String code) {
        return inventory.findByCode(code)
                .map(InventoryMapper.INSTANCE::modelToResponse);
    }

    @Override
    public Flux<InventoryResponse> getList() {
        return inventory.findAll()
                .map(InventoryMapper.INSTANCE::modelToResponse);
    }
}
