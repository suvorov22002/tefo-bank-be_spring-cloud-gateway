package com.pfk.cbs.springcloudgateway.config;

import com.pfk.cbs.springcloudgateway.GatewayConfigProperties;
import com.pfk.cbs.springcloudgateway.config.jwt.TokenIntrospect;
import com.pfk.cbs.springcloudgateway.exception.config.AccessDeniedHandler;
import com.pfk.cbs.springcloudgateway.config.utils.ConfigUtils;
import com.pfk.cbs.springcloudgateway.dto.UrlMethodInfo;
import com.pfk.cbs.springcloudgateway.exception.config.AuthenticationEntryPoint;
import com.pfk.cbs.springcloudgateway.exception.config.AuthenticationFailureHandler;
import com.tefo.library.commonutils.constants.RestEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final GatewayConfigProperties gatewayConfigProperties;
    private final TokenIntrospect tokenIntrospect;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(this::configureExchange)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(
                                HttpMethod.GET,
                                RestEndpoints.BUSINESS_DAY + "/get-open-business-day",
                                RestEndpoints.CORE_SETTINGS_BASE + "/client-settings",
                                RestEndpoints.UNITS + "/basic-info",
                                RestEndpoints.USERS + "/basic-info"
                        )
                        .authenticated()
                        .pathMatchers(
                                HttpMethod.GET,
                                "/dictionary-service/v3/api-docs/**",
                                "/core-bank-settings/v3/api-docs/**", "/identity-service/v3/api-docs/**",
                                "/org-structure/v3/api-docs/**", "/customer-service/v3/api-docs/**",
                                "/transactions-service/v3/api-docs/**"
                        )
                        .permitAll()
                        .anyExchange()
                        .authenticated());

        http.oauth2ResourceServer(oauth2 -> oauth2
                .authenticationFailureHandler(new AuthenticationFailureHandler())
                .opaqueToken(opaqueTokenSpec -> opaqueTokenSpec
                        .introspector(tokenIntrospect)));

        http.exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                .accessDeniedHandler(new AccessDeniedHandler())
                .authenticationEntryPoint(new AuthenticationEntryPoint())
        );

        return http.build();
    }

    private void configureExchange(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec) {
        Map<String, List<String>> permissions = gatewayConfigProperties.getPermissions();
        permissions.forEach((permission, urlAndMethodList) ->
                urlAndMethodList.forEach(urlAndMethod -> {
                    UrlMethodInfo urlMethodInfo = ConfigUtils.parseUrlAndMethod(urlAndMethod);
                    if (permission.contains("/")) {
                        String[] combinedPermissions = permission.split("/");
                        authorizeExchangeSpec.pathMatchers(urlMethodInfo.httpMethod(), urlMethodInfo.path())
                                .hasAnyRole(combinedPermissions);
                    } else {
                        authorizeExchangeSpec.pathMatchers(urlMethodInfo.httpMethod(), urlMethodInfo.path())
                                .hasRole(permission);
                    }
                }));
    }
}
