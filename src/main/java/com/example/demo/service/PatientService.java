package com.example.demo.service;

import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService extends GenericService<Patient, Long> {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        super(patientRepository);
        this.patientRepository = patientRepository; 
    }

    public List<Patient> findByFullNameContaining(String fullName) {
        return patientRepository.findByNameContainingIgnoreCase(fullName);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll(); 
    }
}
