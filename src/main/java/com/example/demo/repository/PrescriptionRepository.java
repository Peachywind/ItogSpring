package com.example.demo.repository;

import com.example.demo.model.Prescription;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends GenericRepository<Prescription, Long> {
    List<Prescription> findByMedicationContainingIgnoreCase(String medication);
}
