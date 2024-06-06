package com.lawencon.payroll.service;

import java.util.List;

import com.lawencon.payroll.dto.generalResponse.DeleteResDto;
import com.lawencon.payroll.dto.notification.NotificationResDto;

public interface NotificationService {
    List<NotificationResDto> getNotifications();

    Integer getNotificationCount();

    void readNotification(String id);

    DeleteResDto deleteNotification(String id); 

    DeleteResDto deleteAllUserNotification(String id);
}
