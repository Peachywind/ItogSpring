package com.example.demo.service;

import com.example.demo.model.Prescription;
import com.example.demo.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService extends GenericService<Prescription, Long> {

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        super(prescriptionRepository);
    }

    public List<Prescription> findByMedicationContaining(String medication) {
        return ((PrescriptionRepository) repository).findByMedicationContainingIgnoreCase(medication);
    }
}
