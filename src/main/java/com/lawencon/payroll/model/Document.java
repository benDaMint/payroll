package com.lawencon.payroll.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "t_r_document")
public class Document extends BaseModel {

    @ManyToOne
    @Column(name = "file_id", nullable = false)
    private File fileId;

    @ManyToOne
    @Column(name = "document_type_id", nullable = false)
    private DocumentType documentTypeId;

    @Column(name = "is_signed_by_sender", nullable = false)
    private Boolean isSignedBySender;

    @Column(name = "is_signed_by_receiver", nullable = false)
    private Boolean isSignedByReceiver;
}