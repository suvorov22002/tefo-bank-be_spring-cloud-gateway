package com.pfk.cbs.springcloudgateway.exception.config;

import com.pfk.cbs.springcloudgateway.exception.utils.ExceptionHandlingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import reactor.core.publisher.Mono;

public class AuthenticationFailureHandler implements ServerAuthenticationFailureHandler {
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException e) {
        return ExceptionHandlingUtils.writeErrorResponse(
                webFilterExchange.getExchange(), HttpStatus.UNAUTHORIZED, e.getMessage()
        );
    }
}
