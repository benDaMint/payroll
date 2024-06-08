package com.lawencon.payroll.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "t_m_company", uniqueConstraints = { @UniqueConstraint(columnNames = { "company_name" }) })

public class Company extends BaseModel {
  @Column(name = "company_name", nullable = false)
  private String companyName;

  @ManyToOne
  @JoinColumn(name = "company_logo_id", nullable = false)
  private File companyLogo;

  @Column(name = "payroll_date", nullable = false)
  private Integer payrollDate;
}
