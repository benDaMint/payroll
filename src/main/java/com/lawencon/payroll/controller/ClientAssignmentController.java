package com.lawencon.payroll.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.payroll.dto.clientAssignment.ClientAssignmentReqDto;
import com.lawencon.payroll.dto.clientAssignment.ClientAssignmentResDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.service.ClientAssignmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("client-assignments")
@RequiredArgsConstructor
public class ClientAssignmentController {
  private final ClientAssignmentService clientAssignmentService;

  @GetMapping("")
  public ResponseEntity<ClientAssignmentResDto> getClientAssignment(
      @PathVariable("id") String id) {
    final var clientAssignmentRes = clientAssignmentService.getById(id);

    return new ResponseEntity<>(clientAssignmentRes, HttpStatus.OK);
  }

  @PostMapping()
  public ResponseEntity<InsertResDto> saveClientAssignment(@RequestBody ClientAssignmentReqDto data) {
    final var insertRes = clientAssignmentService.saveClientAssignment(data);
    return new ResponseEntity<>(insertRes, HttpStatus.CREATED);
  }

  @GetMapping("{clientId}")
  public ResponseEntity<ClientAssignmentResDto> getClientAssignmentByClient(@PathVariable String clientId) {
    final var clientAssignmentRes = clientAssignmentService.getByClientId(clientId);

    return new ResponseEntity<>(clientAssignmentRes, HttpStatus.OK);
  }
  
}
