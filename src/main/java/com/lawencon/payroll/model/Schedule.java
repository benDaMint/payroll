package com.lawencon.payroll.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "t_r_schedule")
public class Schedule extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "client_assignment_id", nullable = false)
    private ClientAssignment clientAssignment;

    @ManyToOne
    @JoinColumn(name = "schedule_request_type_id", nullable = false)
    private ScheduleRequestType scheduleRequestType;
}
