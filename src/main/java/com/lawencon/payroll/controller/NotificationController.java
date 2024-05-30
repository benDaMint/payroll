package com.lawencon.payroll.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.payroll.dto.generalResponse.DeleteResDto;
import com.lawencon.payroll.dto.notification.NotificationResDto;
import com.lawencon.payroll.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("")
    public ResponseEntity<List<NotificationResDto>> getNotifications() {
        final var notificationsRes = notificationService.getNotifications();
        return new ResponseEntity<>(notificationsRes, HttpStatus.OK);
    }

    @GetMapping("count")
    public ResponseEntity<Integer> getNotificationCount() {
        final var notificationCount = notificationService.getNotificationCount();
        return new ResponseEntity<>(notificationCount, HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public void readNotification(@PathVariable String id) {
        notificationService.readNotification(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<DeleteResDto> deleteNotification(@PathVariable String id) {
        final var deleteRes = notificationService.deleteNotification(id);
        return new ResponseEntity<>(deleteRes, HttpStatus.OK);
    }
}
