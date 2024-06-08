package com.lawencon.payroll.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.payroll.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String>
{
  List<Schedule> findByClientAssignmentIdOrderByCreatedAtDesc(String clientAssignmentId);

  Optional<Schedule> findFirstByClientAssignmentIdOrderByCreatedAtDesc(String clientAssignmentId);

}