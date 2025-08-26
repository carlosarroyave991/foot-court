package com.foorcourt.infraestructure.out.feignclient;

import com.foorcourt.domain.model.feignclient.SmsNotificationModel;
import com.foorcourt.domain.spi.ISmsFeignClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsFeignClientAdapter implements ISmsFeignClientPort {
    
    private final SmsFeignClient smsFeignClient;
    
    @Override
    public void sendOrderStatusNotification(SmsNotificationModel smsNotification) {
        smsFeignClient.sendSms(smsNotification);
    }
}