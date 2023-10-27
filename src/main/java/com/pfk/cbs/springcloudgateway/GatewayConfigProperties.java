package com.pfk.cbs.springcloudgateway;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "gateway-config-properties")
@Getter
@Setter
public class GatewayConfigProperties {
    private Map<String, List<String>> permissions = new LinkedHashMap<>();
    private Map<String, List<String>> routes = new HashMap<>();
}
