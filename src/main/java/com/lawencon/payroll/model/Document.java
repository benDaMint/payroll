package com.lawencon.payroll.model;

import java.time.LocalDateTime;

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
@Table(name = "t_r_document")
public class Document extends BaseModel {
    @Column(name = "document_name")
    private String documentName;
    
    @Column(name = "document_directory")
    private String documentDirectory;

    @Column(name = "document_deadline", nullable = false)
    private LocalDateTime documentDeadline;

    @Column(nullable = false)
    private String activity;
    
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(name = "is_signed_by_client")
    private Boolean isSignedByClient;

    @Column(name = "is_signed_by_ps")
    private Boolean isSignedByPs;
}
