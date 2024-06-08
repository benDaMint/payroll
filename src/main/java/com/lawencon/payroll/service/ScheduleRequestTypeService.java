package com.lawencon.payroll.service;

import java.util.List;

import com.lawencon.payroll.dto.scheduleRequestTypeService.ScheduleRequestTypeResDto;

public interface ScheduleRequestTypeService {
  List<ScheduleRequestTypeResDto> getAll();
  ScheduleRequestTypeResDto getById(String id);
}
