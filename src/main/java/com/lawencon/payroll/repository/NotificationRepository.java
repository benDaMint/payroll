package com.lawencon.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.payroll.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

}
