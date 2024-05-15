package com.lawencon.payroll.service;

import com.lawencon.payroll.dto.clientAssignment.ClientAssignmentReqDto;
import com.lawencon.payroll.dto.clientAssignment.ClientAssignmentResDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;

public interface ClientAssignmentService {
  InsertResDto saveClientAssignment(ClientAssignmentReqDto clientAssignmentReqDto);
  ClientAssignmentResDto getById(String id);
}