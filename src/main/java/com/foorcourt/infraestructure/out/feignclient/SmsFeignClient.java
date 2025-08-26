package com.foorcourt.infraestructure.out.feignclient;

import com.foorcourt.domain.model.feignclient.SmsNotificationModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "messenger-service", url = "http://localhost:8082")
public interface SmsFeignClient {
    
    @PostMapping("/api/v1/sms/send")
    void sendSms(@RequestBody SmsNotificationModel smsNotification);
}