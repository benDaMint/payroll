package com.lawencon.payroll.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "t_r_calculated_payroll_documents")
public class CalculatedPayrollDocuments extends BaseModel {
    @Column(name = "document_name")
    private String documentName;
    
    @Column(name = "document_directory")
    private String documentDirectory;

    @Column(nullable = false)
    private String activity;
    
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;
  
}
