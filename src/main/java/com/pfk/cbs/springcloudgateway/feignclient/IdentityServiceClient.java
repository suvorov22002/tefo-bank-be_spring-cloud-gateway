package com.pfk.cbs.springcloudgateway.feignclient;

import com.pfk.cbs.springcloudgateway.dto.UserBasicInfoDto;
import com.tefo.library.commonutils.constants.RestEndpoints;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "identityServiceClient", url = "http://" + "${identity-service.host}" + RestEndpoints.USERS)
public interface IdentityServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/basic-info?{sessionState}")
//    @Cacheable(value = "introspect_token", key = "#sessionState")
    UserBasicInfoDto getCurrentUserPermissionsBasicInfo(@PathVariable String sessionState,
                                                        @RequestHeader("Authorization")
                                                        String bearerToken);
}
