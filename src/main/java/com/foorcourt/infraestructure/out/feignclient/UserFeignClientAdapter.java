package com.foorcourt.infraestructure.out.feignclient;

import com.foorcourt.domain.model.feignclient.UserModel;
import com.foorcourt.domain.spi.IUserFeignClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFeignClientAdapter implements IUserFeignClientPort {
    
    private final UserFeignClient userFeignClient;
    
    @Override
    public UserModel getUserById(Long userId) {
        return userFeignClient.getUserById(userId);
    }
}