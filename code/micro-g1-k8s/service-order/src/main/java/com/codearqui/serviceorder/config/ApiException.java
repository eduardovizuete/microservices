package com.codearqui.serviceorder.config;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
public class ApiException extends Exception {
    private String code;
    private String description;
    private List<String> detail;
    private HttpStatus status;
    private Throwable cause;

    private ApiException(String code, String description, List<String> detail, HttpStatus status, Throwable cause) {
        super();
        this.code = code;
        this.description = description;
        this.detail = detail;
        this.status = status;
        this.cause = cause;
    }
}