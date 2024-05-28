package com.lawencon.payroll.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.payroll.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {
    List<Document> findByScheduleId(String scheduleId);
    List<Document> findByScheduleIdOrderByDocumentDeadlineAsc(String scheduleId);
    Optional<Document> findFirstByScheduleIdOrderByDocumentDeadlineDesc(String scheduleId);
}
