package com.lawencon.payroll.dto.report;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportResDto {
    private String id;
    private String activity;
    private String documentName;
    private LocalDateTime documentDeadline;
    private LocalDateTime updatedAt;
    private String information;
}
