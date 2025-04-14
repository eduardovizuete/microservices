package com.codearqui.serviceinventory.model.mapper;

import com.codearqui.serviceinventory.dto.InventoryRequest;
import com.codearqui.serviceinventory.dto.InventoryResponse;
import com.codearqui.serviceinventory.model.entity.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    @Mapping(target = "code", source = "idProduct")
    ProductModel requestToModel(InventoryRequest inventoryRequest);

    @Mapping(target = "idProduct", source = "code")
    InventoryResponse modelToResponse(ProductModel productModel);
}
