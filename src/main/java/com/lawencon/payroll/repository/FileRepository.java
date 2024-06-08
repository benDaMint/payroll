package com.lawencon.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.payroll.model.File;

@Repository
public interface FileRepository extends JpaRepository<File, String> {

}
