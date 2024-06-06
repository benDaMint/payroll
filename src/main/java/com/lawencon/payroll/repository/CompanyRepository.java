package com.lawencon.payroll.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lawencon.payroll.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    Company findByCompanyName(String companyName);

    @Query(value = "SELECT c FROM Company c "
                    + "ORDER BY c.companyName ASC ")
    List<Company> findAll();

    @Query(value = "SELECT c.companyName FROM Company c "
                    + "WHERE c.id != :id AND LOWER(c.companyName) = LOWER(:name) ")
    Optional<String> getCompanyNameByIdAndName(@Param("id") String id, @Param("name") String name);
}
