package com.pfk.cbs.springcloudgateway.config;

import com.pfk.cbs.springcloudgateway.GatewayConfigProperties;
import com.pfk.cbs.springcloudgateway.config.utils.ConfigUtils;
import com.pfk.cbs.springcloudgateway.dto.UrlMethodInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {

    private final GatewayConfigProperties gatewayConfigProperties;
    private final static String HTTP_URL = "http://";

    @Bean
    public RouteLocator routerBuilder(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        gatewayConfigProperties.getRoutes().forEach((serviceName, methodUrlList) -> methodUrlList
                .forEach(urlAndMethod -> {
                    UrlMethodInfo urlMethodInfo = ConfigUtils.parseUrlAndMethod(urlAndMethod);
                    routes.route(serviceName, predicateSpec ->
                            predicateSpec
                                    .path(urlMethodInfo.path())
                                    .and()
                                    .method(urlMethodInfo.httpMethod())
                                    .and()
                                    .uri(HTTP_URL + serviceName));
                }));
        return routes.build();
    }
}
