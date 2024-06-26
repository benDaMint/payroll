package com.lawencon.payroll.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.payroll.dto.generalResponse.DeleteResDto;
import com.lawencon.payroll.dto.generalResponse.UpdateResDto;
import com.lawencon.payroll.dto.notification.NotificationResDto;
import com.lawencon.payroll.model.Notification;
import com.lawencon.payroll.repository.NotificationRepository;
import com.lawencon.payroll.repository.NotificationTemplateRepository;
import com.lawencon.payroll.service.NotificationService;
import com.lawencon.payroll.service.PrincipalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;

    private final PrincipalService principalService;

    @Override
    public List<NotificationResDto> getNotifications() {
        final var notificationsRes = new ArrayList<NotificationResDto>();

        final List<Notification> notifications = notificationRepository.findAllByUserId(principalService.getUserId());

        notifications.forEach(notification -> {
            final var notificationRes = new NotificationResDto();

            notificationRes.setNotificationId(notification.getId());
            notificationRes.setRouteLink(notification.getRouteLink());
            notificationRes.setIsActive(notification.getIsActive());

            final var template = notification.getNotificationTemplate();

            notificationRes.setNotificationCode(template.getNotificationCode());
            notificationRes.setNotificationHeader(template.getNotificationHeader());
            notificationRes.setNotificationBody(template.getNotificationBody());

            notificationsRes.add(notificationRes);
        });

        return notificationsRes;
    }

    @Override
    public Integer getNotificationCount() {
        return notificationRepository.getCountById(principalService.getUserId());
    }

    @Override
    public void readNotification(String id) {
        var notification = notificationRepository.findById(id).get();
        notification.setIsActive(false);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public UpdateResDto readAllUserNotification(String id) {
        final var notifications = notificationRepository.findAllByUserId(id);
        final var updateRes = new UpdateResDto();

        notifications.forEach(notification -> {
            notification.setIsActive(false);
        });

        notificationRepository.saveAllAndFlush(notifications);

        updateRes.setMessage("Notifications Read");
        updateRes.setVersion(null);

        return updateRes;
    }

    @Override
    @Transactional
    public DeleteResDto deleteNotification(String id) {
        var notification = notificationRepository.findById(id).get();
        var notificationTemplateId = "";
        if (notification.getNotificationTemplate().getNotificationCode().equals("NTCUS")) {
            notificationTemplateId = notification.getNotificationTemplate().getId();
            notificationRepository.deleteById(id);
            notificationTemplateRepository.deleteById(notificationTemplateId);
        } else {
            notificationRepository.deleteById(id);
        }
        final var deleteRes = new DeleteResDto();

        return deleteRes;
    }

    @Override
    @Transactional
    public DeleteResDto deleteAllUserNotification(String id) {
        notificationRepository.deleteByUserId(id);
        
        final var deleteRes = new DeleteResDto();

        deleteRes.setMessage("Notifications Has Been Cleared");
        return deleteRes;
    }
}
