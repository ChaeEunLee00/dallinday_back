package com.by.dallinday.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(BusinessLogicException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", e.getExceptionCode().name());
        body.put("message", e.getMessage());
        return ResponseEntity.status(e.getExceptionCode().getStatus()).body(body);
    }
}
