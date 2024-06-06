package com.lawencon.payroll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lawencon.payroll.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
  @Query(value = "SELECT n FROM Notification n "
                + "WHERE n.user.id = :id "
                + "ORDER BY n.createdAt DESC ")
  List<Notification> findAllByUserId(@Param("id") String userId);

  @Query(value = "SELECT COUNT(n) FROM Notification n "
                + "WHERE n.user.id = :id "
                + "AND n.isActive = TRUE ")
  Integer getCountById(@Param("id") String id);

  Long deleteByUserId(String userId);
}
