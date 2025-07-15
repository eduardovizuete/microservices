package com.codearqui.serviceinventory.repository;

import com.codearqui.serviceinventory.model.entity.ProductModel;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface InventoryRepository extends R2dbcRepository<ProductModel, Integer> {
    Mono<Boolean> existsByCode(String code);
    Mono<ProductModel> findByCode(String code);
}
