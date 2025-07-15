package com.codearqui.serviceorder.config;

import com.codearqui.serviceorder.model.dto.ErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ErrorExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Mono<Void> handleAllExceptions(Exception ex, ServerWebExchange exchange) throws JsonProcessingException {
        log.error("Error no contralado", ex);

        ErrorException errorResponse = new ErrorException("ER00", "Error no controlado", List.of(ex.getMessage()));

        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer dataBuffer = bufferFactory.wrap(new ObjectMapper().writeValueAsBytes(errorResponse));

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    /// Query Params
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<Void> handleAllExceptions(WebExchangeBindException ex, ServerWebExchange exchange) throws JsonProcessingException {
        List<String> errors = ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        var errorResponse = new ErrorException(ex.getReason(), ex.getBody().getDetail(), errors);

        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer dataBuffer = bufferFactory.wrap(new ObjectMapper().writeValueAsBytes(errorResponse));

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<Void> handleAllExceptions(ServerWebInputException ex, ServerWebExchange exchange) throws JsonProcessingException {
        List<String> errors = List.of(ex.getCause().getMessage());

        var errorResponse = new ErrorException(ex.getReason(), ex.getBody().getDetail(), errors);

        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer dataBuffer = bufferFactory.wrap(new ObjectMapper().writeValueAsBytes(errorResponse));

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    @ExceptionHandler(ApiException.class)
    public Mono<Void> handleAllExceptions(ApiException ex, ServerWebExchange exchange) throws JsonProcessingException {
        List<String> errors = ex.getDetail() != null ? ex.getDetail() : new ArrayList<>();
        if(ex.getCause() != null) {
            errors.add(ex.getCause().getMessage());
        }

        var errorResponse = new ErrorException(
                ex.getCode() != null ? ex.getCode() : "ER000",
                ex.getDescription() != null ? ex.getDescription() : "Error no controlado",
                errors
        );

        exchange.getResponse().setStatusCode(ex.getStatus() != null ? ex.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // Convertir el ErrorException a JSON
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer dataBuffer = bufferFactory.wrap(new ObjectMapper().writeValueAsBytes(errorResponse));

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}