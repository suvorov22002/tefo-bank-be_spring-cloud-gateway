package com.pfk.cbs.springcloudgateway.exception.config;

import com.pfk.cbs.springcloudgateway.exception.utils.ExceptionHandlingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class AuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        return ExceptionHandlingUtils.writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED, e.getMessage());
    }
}
