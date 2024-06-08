package com.lawencon.payroll.dto.clientAssignment;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientAssignmentReqDto {
  private String psId;
  private List<String> clients;
}
