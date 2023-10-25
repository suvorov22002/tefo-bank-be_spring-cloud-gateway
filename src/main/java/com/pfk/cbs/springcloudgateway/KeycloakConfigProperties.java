package com.pfk.cbs.springcloudgateway;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakConfigProperties {
    private String introspectUri;
    private String clientId;
    private String clientSecret;
}
