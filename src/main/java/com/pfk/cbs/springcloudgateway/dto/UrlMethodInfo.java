package com.pfk.cbs.springcloudgateway.dto;

import org.springframework.http.HttpMethod;

public record UrlMethodInfo(HttpMethod httpMethod, String path) {
}
