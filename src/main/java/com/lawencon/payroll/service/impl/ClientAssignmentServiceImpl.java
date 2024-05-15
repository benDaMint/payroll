package com.lawencon.payroll.service.impl;
import java.util.Optional;

import com.lawencon.payroll.dto.clientAssignment.ClientAssignmentReqDto;
import com.lawencon.payroll.dto.clientAssignment.ClientAssignmentResDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.model.ClientAssignment;
import com.lawencon.payroll.repository.ClientAssignmentRepository;
import com.lawencon.payroll.service.ClientAssignmentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientAssignmentServiceImpl implements ClientAssignmentService  {

  private ClientAssignmentRepository clientAssignmentRepository;

  @Override
  public ClientAssignmentResDto getById(String id) {
    final Optional<ClientAssignment> clientAssignment = clientAssignmentRepository.findById(id);

    final var clientAssignmentResDto = new ClientAssignmentResDto();

    final var payrollServiceId = clientAssignment.get().getPayrollService().getId();
    final var clientId = clientAssignment.get().getClient().getId();

    clientAssignmentResDto.setId(id);
    clientAssignmentResDto.setClientId(clientId);
    clientAssignmentResDto.setPsId(payrollServiceId);
    
    return clientAssignmentResDto;
  }

  @Override
  public InsertResDto saveClientAssignment(ClientAssignmentReqDto clientAssignmentReq) {
    final var insertRes = new InsertResDto();

    final var clientAssignment = new ClientAssignment();

    final var clientId = clientAssignmentReq.getClientId();
    final var payrollServiceId = clientAssignmentReq.getPsId(); 


    clientAssignment.setClient(null);
    clientAssignment.setPayrollService(null);

    return insertRes;
  }
}