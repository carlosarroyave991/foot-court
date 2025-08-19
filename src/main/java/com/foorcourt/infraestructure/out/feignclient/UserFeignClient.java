package com.foorcourt.infraestructure.out.feignclient;

import com.foorcourt.domain.model.feignclient.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${feign.user-service.url}")
public interface UserFeignClient {
    
    @GetMapping("/api/v1/user/{userId}")
    UserModel getUserById(@PathVariable("userId") Long userId);
}
