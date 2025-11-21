package com.codearqui.serviceorder.model.dto;

import java.util.List;

public record ErrorException(
        String code,
        String description,
        List<String> detail
) { }