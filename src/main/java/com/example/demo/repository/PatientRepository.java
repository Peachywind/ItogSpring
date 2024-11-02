package com.example.demo.repository;

import com.example.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends GenericRepository<Patient, Long> {
    List<Patient> findByNameContainingIgnoreCase(String name);
}
