package com.lawencon.payroll.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "t_m_schedule_request_type")
public class ScheduleRequestType extends BaseModel {

    @Column(name = "schedule_request_code", nullable = false)
    private String scheduleRequestCode;

    @Column(name = "schedule_request_name", nullable = false)
    private String scheduleRequestName;
}
