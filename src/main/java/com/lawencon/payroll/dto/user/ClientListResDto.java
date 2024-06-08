package com.lawencon.payroll.dto.user;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientListResDto {
    private String psUserName;
    private List<ClientResDto> assignedClients;
    private List<ClientResDto> unassignedClients;
}
