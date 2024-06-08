package com.lawencon.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.payroll.model.NotificationTemplate;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, String>  {
  NotificationTemplate findByNotificationCode(String code);
}
