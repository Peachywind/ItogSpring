package com.example.demo.repository;

import com.example.demo.model.Feedback;
import com.example.demo.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends GenericRepository<Feedback, Long> {
    List<Feedback> findByCommentsContainingIgnoreCase(String comments);
    List<Feedback> findByPatient(Patient patient); // Используем объект Patient
    List<Feedback> findByAppointmentId(Long appointmentId);
}
