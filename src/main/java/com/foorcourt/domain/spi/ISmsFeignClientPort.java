package com.foorcourt.domain.spi;

import com.foorcourt.domain.model.feignclient.SmsNotificationModel;

public interface ISmsFeignClientPort {
    void sendOrderStatusNotification(SmsNotificationModel smsNotification);
}