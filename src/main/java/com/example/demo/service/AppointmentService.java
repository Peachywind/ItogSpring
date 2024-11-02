package com.example.demo.service;

import com.example.demo.model.Appointment;
import com.example.demo.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService extends GenericService<Appointment, Long> {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        super(appointmentRepository);
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> findByPatientNameContaining(String name) {
        return appointmentRepository.findByPatient_NameContainingIgnoreCase(name);
    }

    public List<Appointment> findByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
}
