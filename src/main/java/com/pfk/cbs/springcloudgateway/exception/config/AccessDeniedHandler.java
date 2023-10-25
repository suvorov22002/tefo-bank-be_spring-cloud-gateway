package com.pfk.cbs.springcloudgateway.exception.config;

import com.pfk.cbs.springcloudgateway.exception.utils.ExceptionHandlingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class AccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
        return ExceptionHandlingUtils.writeErrorResponse(exchange, HttpStatus.FORBIDDEN, e.getMessage());
    }
}
