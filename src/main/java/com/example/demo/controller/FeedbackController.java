package com.example.demo.controller;

import com.example.demo.model.Appointment;
import com.example.demo.model.Feedback;
import com.example.demo.model.Patient;
import com.example.demo.service.FeedbackService;
import com.example.demo.service.PatientService;
import com.example.demo.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    public FeedbackController(FeedbackService feedbackService, PatientService patientService, AppointmentService appointmentService) {
        this.feedbackService = feedbackService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping
    public String index(Model model) {
        model.addAttribute("feedbacks", feedbackService.findAll());
        return "feedback/index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/new")
    public String newFeedback(Model model) {
        model.addAttribute("feedback", new Feedback());
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("appointments", appointmentService.findAll());
        return "feedback/new";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public String create(@Valid Feedback feedback, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("appointments", appointmentService.findAll());
            return "feedback/new";  // возвращаем на форму с ошибками
        }
        feedbackService.save(feedback);
        model.addAttribute("message", "Фидбэк успешно создан!");
        return "redirect:/feedback";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/edit")
    public String editFeedback(@PathVariable("id") Long id, Model model) {
        Optional<Feedback> feedbackOpt = feedbackService.findById(id);
        if (feedbackOpt.isPresent()) {
            Feedback feedback = feedbackOpt.get();
            List<Patient> patients = patientService.findAll();
            List<Appointment> appointments = appointmentService.findAll();
            
            // Убедитесь, что передаете правильные данные в модель
            model.addAttribute("feedback", feedback);
            model.addAttribute("patients", patients);
            model.addAttribute("appointments", appointments);
            model.addAttribute("currentAppointment", feedback.getAppointment());
    
            return "feedback/edit";
        } else {
            return "redirect:/feedback";
        }
    }
    

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}")
    public String updateFeedback(
        @PathVariable("id") Long id,
        @Valid @ModelAttribute("feedback") Feedback feedback,
        BindingResult result,
        Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("appointments", appointmentService.findAll());
            return "feedback/edit";
        }

        Optional<Feedback> existingFeedback = feedbackService.findById(id);
        if (existingFeedback.isPresent()) {
            Feedback updatedFeedback = existingFeedback.get();
            
            // Обновляем поля с проверкой на null
            updatedFeedback.setComments(feedback.getComments() != null ? feedback.getComments() : "");
            updatedFeedback.setRating(feedback.getRating() != null ? feedback.getRating() : 0);
            updatedFeedback.setPatient(feedback.getPatient());
            updatedFeedback.setAppointment(feedback.getAppointment());
            updatedFeedback.setFeedbackDate(feedback.getFeedbackDate());

            feedbackService.save(updatedFeedback);
        } else {
            model.addAttribute("error", "Feedback not found");
            return "feedback/edit";
        }

        return "redirect:/feedback";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteFeedback(@PathVariable("id") Long id) {
        feedbackService.deleteById(id);
        return "redirect:/feedback";
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public String showFeedback(@PathVariable Long id, Model model) {
        Optional<Feedback> optionalFeedback = feedbackService.findById(id);
        if (optionalFeedback.isPresent()) {
            model.addAttribute("feedback", optionalFeedback.get());
        } else {
            model.addAttribute("error", "Фидбэк не найден");
        }
        return "feedback/show";
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/search")
    public String search(@RequestParam("patientId") Long patientId, Model model) {
        List<Feedback> feedbacks = feedbackService.findByPatient(patientService.findById(patientId).orElse(null));
        model.addAttribute("feedbacks", feedbacks);
        return "feedback/index";
    }
}
