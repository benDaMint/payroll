package com.lawencon.payroll.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lawencon.payroll.dto.scheduleRequestTypeService.ScheduleRequestTypeResDto;
import com.lawencon.payroll.model.ScheduleRequestType;
import com.lawencon.payroll.repository.ScheduleRequestTypeRepository;
import com.lawencon.payroll.service.ScheduleRequestTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleRequestServiceImpl implements ScheduleRequestTypeService{

  private ScheduleRequestTypeRepository scheduleRequestTypeRepository;

  @Override
  public ScheduleRequestTypeResDto getById(String id) {
    final Optional<ScheduleRequestType> scheduleRequestType = scheduleRequestTypeRepository.findById(id);
    final var scheduleTypeRes = new ScheduleRequestTypeResDto();
    
    if(scheduleRequestType.isPresent()) {
      final var scheduleRequestId = scheduleRequestType.get().getId();
      final var scheduleRequestCode = scheduleRequestType.get().getScheduleRequestCode();
      final var scheduleRequestName = scheduleRequestType.get().getScheduleRequestName();

      scheduleTypeRes.setScheduleRequestCode(scheduleRequestCode);
      scheduleTypeRes.setScheduleRequestName(scheduleRequestName);
      scheduleTypeRes.setScheduleRequestId(scheduleRequestId);
    }

    return scheduleTypeRes;
  }

  @Override
  public List<ScheduleRequestTypeResDto> getAll() {
    final var scheduleRequestTypes = scheduleRequestTypeRepository.findAll();
    final var scheduleTypesRes = new ArrayList<ScheduleRequestTypeResDto>();

    scheduleRequestTypes.forEach(scheduleRequest -> {
      final var scheduleTypeRes = new ScheduleRequestTypeResDto();

      final var scheduleRequestId = scheduleRequest.getId();
      final var scheduleRequestCode = scheduleRequest.getScheduleRequestCode();
      final var scheduleRequestName = scheduleRequest.getScheduleRequestName();

      scheduleTypeRes.setScheduleRequestCode(scheduleRequestCode);
      scheduleTypeRes.setScheduleRequestName(scheduleRequestName);
      scheduleTypeRes.setScheduleRequestId(scheduleRequestId);

      scheduleTypesRes.add(scheduleTypeRes);
    });

    return scheduleTypesRes;
  }
}
