package com.pfk.cbs.springcloudgateway.config.utils;

import com.pfk.cbs.springcloudgateway.dto.UrlMethodInfo;
import org.springframework.http.HttpMethod;

public class ConfigUtils {
    public static UrlMethodInfo parseUrlAndMethod(String urlAndMethod) {
        String[] parts = urlAndMethod.split(":");
        String httpMethod = parts[0];
        String path = parts[1];
        return new UrlMethodInfo(HttpMethod.valueOf(httpMethod), path);
    }
}
