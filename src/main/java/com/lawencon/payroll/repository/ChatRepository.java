package com.lawencon.payroll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lawencon.payroll.model.Chat;
import com.lawencon.payroll.model.User;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {
    List<Chat> findByRecipientId(String recipientId);

    List<Chat> findAll();

    @Query("SELECT c FROM Chat c WHERE c.recipientId = :recipientId AND c.createdBy = :createdBy ORDER BY c.createdAt ASC")
    List<Chat> findByRecipientIdOrCreatedBy(@Param("recipientId") User recipientId, @Param("createdBy") String createdBy);
}
