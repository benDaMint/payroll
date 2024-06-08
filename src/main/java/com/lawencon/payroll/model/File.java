package com.lawencon.payroll.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "t_m_file")
public class File extends BaseModel {
    @Column(name = "file_content", nullable = false)
	private String fileContent;

    @Column(name = "file_extension", nullable = false)
    private String fileExtension;
}
