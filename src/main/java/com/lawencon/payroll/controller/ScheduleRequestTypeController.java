package com.lawencon.payroll.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.payroll.dto.scheduleRequestTypeService.ScheduleRequestTypeResDto;
import com.lawencon.payroll.service.ScheduleRequestTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("schedule-request-types")
public class ScheduleRequestTypeController {
  private final ScheduleRequestTypeService scheduleRequestService;

  @GetMapping("")
  public ResponseEntity<List<ScheduleRequestTypeResDto>> getAllScheduleRequestType() {
    final var scheduleRequestTypeRes = scheduleRequestService.getAll();

    return new ResponseEntity<>(scheduleRequestTypeRes, HttpStatus.OK);
  }
}