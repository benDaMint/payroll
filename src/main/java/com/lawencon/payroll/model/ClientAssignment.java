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
@Table(name = "t_r_client_assignment")
public class ClientAssignment extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "ps_id", nullable = false)
    private User psId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User clientId;

}
