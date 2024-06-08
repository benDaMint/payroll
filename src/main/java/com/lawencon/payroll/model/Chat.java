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
@Table(name = "t_r_chat")
public class Chat extends BaseModel {

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipientId;
}
