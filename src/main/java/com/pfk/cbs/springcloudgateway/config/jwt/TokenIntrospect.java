package com.pfk.cbs.springcloudgateway.config.jwt;

import com.pfk.cbs.springcloudgateway.KeycloakConfigProperties;
import com.pfk.cbs.springcloudgateway.feignclient.IdentityServiceClient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusReactiveOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

import java.util.stream.Collectors;

@Component
public class TokenIntrospect implements ReactiveOpaqueTokenIntrospector {

    private final IdentityServiceClient identityServiceClient;
    private final ReactiveOpaqueTokenIntrospector delegate;
    private static final String BEARER = "Bearer ";
    private static final String SESSION_STATE_ATTRIBUTE = "session_state";
    private static final String ROLE_PREFIX = "ROLE_";

    public TokenIntrospect(IdentityServiceClient identityServiceClient, KeycloakConfigProperties keycloakConfigProperties) {
        this.identityServiceClient = identityServiceClient;
        this.delegate = new NimbusReactiveOpaqueTokenIntrospector(
                keycloakConfigProperties.getIntrospectUri(),
                keycloakConfigProperties.getClientId(),
                keycloakConfigProperties.getClientSecret()
        );
    }

    @Override
    public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
        return this.delegate.introspect(token)
                .map(principal -> new DefaultOAuth2AuthenticatedPrincipal(
                                principal.getName(), principal.getAttributes(), extractAuthorities(principal, token)
                        )
                );
    }

    private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal, String token) {
        return identityServiceClient.getCurrentUserPermissionsBasicInfo(
                        principal.getAttribute(SESSION_STATE_ATTRIBUTE),
                        BEARER + token
                )
                .getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(ROLE_PREFIX + permission.getCode()))
                .collect(Collectors.toList());
    }
}
