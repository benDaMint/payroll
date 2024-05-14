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
@Table(name = "t_m_user")
public class User extends BaseModel {
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "pwd", nullable = false)
    private String password;

    @ManyToOne
    @Column(name = "profile_picture_id", nullable = false)
    private File profilePictureId;

    @ManyToOne
    @Column(name = "role_id", nullable = false)
    private Role roleId;
}
