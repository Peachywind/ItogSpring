package com.example.demo.controller;

import com.example.demo.model.Appointment;
import com.example.demo.model.Feedback;
import com.example.demo.model.Patient;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.PatientService;
import com.example.demo.service.UserService;
import com.example.demo.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final UserService userService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, PatientService patientService, UserService userService) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping()
    public String index(Model model) {
        List<Appointment> appointments = appointmentService.findAll(); // Получаем все записи
        model.addAttribute("appointments", appointments); // Добавляем записи в модель
        return "appointments/index"; // Возвращаем шаблон
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/new")
    public String newAppointment(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", userService.getAllDoctors());
        return "appointments/new";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public String create(
        @ModelAttribute("appointment") @Valid Appointment appointment,
        BindingResult result,
        Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", userService.getAllDoctors());
            return "appointments/new";
        }

        // Получение объектов пациента и доктора по их ID
        Patient patient = patientService.findById(appointment.getPatient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Пациент не найден"));
        User doctor = userService.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Доктор не найден"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointmentService.save(appointment);

        return "redirect:/appointments";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/edit")
    public String editAppointment(@PathVariable("id") Long id, Model model) {
        Optional<Appointment> appointment = appointmentService.findById(id);
        if (appointment.isPresent()) {
            model.addAttribute("appointment", appointment.get());
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", userService.getAllDoctors());
            return "appointments/edit";
        } else {
            return "redirect:/appointments";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}")
    public String updateAppointment(@PathVariable("id") Long id, @ModelAttribute("appointment") @Valid Appointment appointment, BindingResult result) {
        if (result.hasErrors()) {
            return "appointments/edit";
        }
        appointmentService.update(id, appointment);
        return "redirect:/appointments";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteAppointment(@PathVariable("id") Long id) {
        appointmentService.deleteById(id);
        return "redirect:/appointments";
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/search")
    public String search(@RequestParam("patientId") Long patientId, Model model) {
        // Попытка найти все записи по ID пациента
        List<Appointment> appointments = appointmentService.findByPatientId(patientId);
        
        // Добавляем найденные записи в модель
        model.addAttribute("appointments", appointments);
        
        return "appointments/index"; // Возвращаем страницу со списком записей
    }
    
    
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public String showAppointment(@PathVariable("id") Long id, Model model) {
        Optional<Appointment> appointment = appointmentService.findById(id);
        if (appointment.isPresent()) {
            model.addAttribute("appointment", appointment.get());
            model.addAttribute("feedback", new Feedback()); // Убедитесь, что вы передаете объект feedback
            return "appointments/show";
        } else {
            return "redirect:/appointments";
        }
    }
    
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/feedback/new")
    public String newFeedback(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "feedback/new";
    }
}
