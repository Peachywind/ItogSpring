package com.example.demo.controller;

import com.example.demo.model.Appointment;
import com.example.demo.model.Patient;
import com.example.demo.model.Prescription;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.PatientService;
import com.example.demo.service.PrescriptionService;
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
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService, PatientService patientService, AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("prescriptions", prescriptionService.findAll());
        return "prescriptions/index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/new")
    public String newPrescription(Model model) {
        model.addAttribute("prescription", new Prescription());
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("appointments", appointmentService.findAll());
        return "prescriptions/new";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public String create(@ModelAttribute("prescription") @Valid Prescription prescription, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("appointments", appointmentService.findAll());
            return "prescriptions/new";
        }
        prescriptionService.save(prescription);
        return "redirect:/prescriptions";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/edit")
    public String editPrescription(@PathVariable("id") Long id, Model model) {
        Optional<Prescription> prescription = prescriptionService.findById(id);
        if (prescription.isPresent()) {
            model.addAttribute("prescription", prescription.get());
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("appointments", appointmentService.findAll());
            return "prescriptions/edit";
        } else {
            return "redirect:/prescriptions";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}")
    public String updatePrescription(@PathVariable("id") Long id, @ModelAttribute("prescription") @Valid Prescription prescription, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("appointments", appointmentService.findAll());
            return "prescriptions/edit";
        }
        prescriptionService.update(id, prescription);
        return "redirect:/prescriptions";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/delete")
    public String deletePrescription(@PathVariable("id") Long id) {
        prescriptionService.deleteById(id);
        return "redirect:/prescriptions";
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/search")
    public String search(@RequestParam("medication") String medication, Model model) {
        List<Prescription> prescriptions = prescriptionService.findByMedicationContaining(medication);
        model.addAttribute("prescriptions", prescriptions);
        return "prescriptions/index";
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public String showPrescription(@PathVariable("id") Long id, Model model) {
        Optional<Prescription> prescription = prescriptionService.findById(id);
        if (prescription.isPresent()) {
            model.addAttribute("prescription", prescription.get());
            return "prescriptions/show";
        } else {
            return "redirect:/prescriptions";
        }
    }
}
