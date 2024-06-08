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
@Table(name = "t_m_user", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
public class User extends BaseModel {
    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    private String email;

    @Column(name = "pwd", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "profile_picture_id", nullable = false)
    private File profilePictureId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company companyId;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role roleId;

    @Column(name = "phone_no", nullable = false)
    private String phoneNumber;
}
