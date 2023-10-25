package com.pfk.cbs.springcloudgateway.exception.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tefo.library.commonutils.exception.utils.ExceptionDetails;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ExceptionHandlingUtils {

    public static Mono<Void> writeErrorResponse(ServerWebExchange exchange, HttpStatus status, String errorMessage) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] responseBytes = serializeErrorResponseToJson(errorMessage);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private static byte[] serializeErrorResponseToJson(String errorMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsBytes(getExceptionDetailsObject(errorMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static ExceptionDetails getExceptionDetailsObject(String message) {
        return ExceptionDetails.builder()
                .message(message)
                .build();
    }
}
