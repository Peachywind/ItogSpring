package com.example.demo.service;

import com.example.demo.model.Feedback;
import com.example.demo.model.Patient;
import com.example.demo.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService extends GenericService<Feedback, Long> {

    public FeedbackService(FeedbackRepository feedbackRepository) {
        super(feedbackRepository);
    }

    public List<Feedback> findByCommentsContaining(String comments) {
        return ((FeedbackRepository) repository).findByCommentsContainingIgnoreCase(comments);
    }

    public List<Feedback> findByPatient(Patient patient) { // Метод с использованием объекта Patient
        return ((FeedbackRepository) repository).findByPatient(patient);
    }

    public List<Feedback> findByAppointmentId(Long appointmentId) {
        return ((FeedbackRepository) repository).findByAppointmentId(appointmentId);
    }
}
