package com.foorcourt.domain.spi;

import com.foorcourt.domain.model.feignclient.UserModel;

public interface IUserFeignClientPort {
    UserModel getUserById(Long userId);
}
