package com.lawencon.payroll.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "t_m_role", uniqueConstraints = { @UniqueConstraint(columnNames = { "role_code", "role_name" }) })
public class Role extends BaseModel {
	@Column(name = "role_code", nullable = false)
	private String roleCode;

	@Column(name = "role_name", nullable = false)
	private String roleName;
}
