package com.lawencon.payroll.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lawencon.payroll.dto.clientAssignment.ClientAssignmentReqDto;
import com.lawencon.payroll.dto.clientAssignment.ClientAssignmentResDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.model.ClientAssignment;
import com.lawencon.payroll.repository.ClientAssignmentRepository;
import com.lawencon.payroll.repository.UserRepository;
import com.lawencon.payroll.service.ClientAssignmentService;
import com.lawencon.payroll.service.PrincipalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientAssignmentServiceImpl implements ClientAssignmentService {

  private final ClientAssignmentRepository clientAssignmentRepository;
  private final UserRepository userRepository;
  private final PrincipalService principalService;

  @Override
  public ClientAssignmentResDto getById(String id) {
    final Optional<ClientAssignment> clientAssignment = clientAssignmentRepository.findById(id);

    final var clientAssignmentResDto = new ClientAssignmentResDto();

    final var payrollServiceId = clientAssignment.get().getPsId().getId();
    final var clientId = clientAssignment.get().getClientId().getId();

    clientAssignmentResDto.setId(id);
    clientAssignmentResDto.setClientId(clientId);
    clientAssignmentResDto.setPsId(payrollServiceId);

    return clientAssignmentResDto;
  }

  @Override
  public InsertResDto saveClientAssignment(ClientAssignmentReqDto data) {
    final var psId = data.getPsId();

    final var clients = data.getClients();

    clients.forEach(client -> {
      final var clientAssignment = new ClientAssignment();

      clientAssignment.setClientId(userRepository.findById(client).get());
      clientAssignment.setPsId(userRepository.findById(psId).get());
      clientAssignment.setCreatedBy(principalService.getUserId());

      clientAssignmentRepository.save(clientAssignment);
    });

    final var insertRes = new InsertResDto();

    insertRes.setMessage("Insert Success");

    return insertRes;
  }

  @Override
  public Integer getTotalClients(String id) {
    return clientAssignmentRepository.getCountClientIdByPsId(id);
  }
}