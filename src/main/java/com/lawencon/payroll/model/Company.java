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
@Table(name = "t_m_company")
<<<<<<< HEAD
public class Company extends BaseModel {
  @Column(name = "company_name", nullable = false)
=======
public class Company extends BaseModel{
  @Column(name = "company_name")
>>>>>>> add_schedule
  private String companyName;

  @ManyToOne
  @JoinColumn(name = "company_logo_id", nullable = false)
  private File companyLogo;

  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  private User clientId;

  @Column(name = "payroll_date", nullable = false)
  private LocalDateTime payrollDate;
}
