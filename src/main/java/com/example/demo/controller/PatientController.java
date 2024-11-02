package com.example.demo.controller;

import com.example.demo.model.Patient;
import com.example.demo.service.PatientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("")
    public String index(Model model) {
        logger.info("Fetching all patients");
        List<Patient> patients = patientService.findAll();
        model.addAttribute("patients", patients);
        return "patients/index"; 
    }

    @GetMapping("/new")
    public String newPatient(Model model) {
        logger.info("Navigating to new patient form");
        model.addAttribute("patient", new Patient());
        return "patients/new";
    }

    @PostMapping("")
    public String create(@ModelAttribute("patient") @Valid Patient patient, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Validation errors occurred while creating a patient: {}", result.getAllErrors());
            return "patients/new";
        }
        patientService.save(patient);
        logger.info("Patient {} created successfully", patient.getName());
        return "redirect:/patients"; 
    }

    @GetMapping("/{id}/edit")
    public String editPatient(@PathVariable("id") Long id, Model model) {
        logger.info("Editing patient with ID: {}", id);
        Optional<Patient> patient = patientService.findById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            return "patients/edit"; 
        } else {
            logger.warn("Patient with ID: {} not found", id);
            return "redirect:/patients"; 
        }
    }

    @PostMapping("/{id}/edit")
    public String updatePatient(@PathVariable("id") Long id, 
                                @ModelAttribute("patient") @Valid Patient patient, 
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Validation errors occurred while updating patient ID {}: {}", id, result.getAllErrors());
            return "patients/edit"; 
        }
    
        // Проверка и установка даты рождения
        try {
            LocalDate birthDate = LocalDate.parse(patient.getBirthDate().toString(), DateTimeFormatter.ISO_LOCAL_DATE);
            patient.setBirthDate(birthDate);
        } catch (Exception e) {
            logger.error("Invalid date format for patient ID {}: {}", id, e.getMessage());
            model.addAttribute("dateError", "Invalid date format. Please use YYYY-MM-DD.");
            return "patients/edit";
        }
    
        patientService.update(id, patient); 
        logger.info("Patient ID {} updated successfully", id);
        return "redirect:/patients"; 
    }
    

    @PostMapping("/{id}/delete") 
    public String deletePatient(@PathVariable("id") Long id) {
        logger.info("Deleting patient with ID: {}", id);
        patientService.deleteById(id);
        return "redirect:/patients"; 
    }

    @GetMapping("/{id}")
    public String showPatient(@PathVariable("id") Long id, Model model) {
        logger.info("Showing patient details for ID: {}", id);
        Optional<Patient> patient = patientService.findById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            return "patients/show"; 
        } else {
            logger.warn("Patient with ID: {} not found", id);
            return "redirect:/patients"; 
        }
    }

    @GetMapping("/search")
    public String search(@RequestParam("name") String name, Model model) {
        logger.info("Searching for patients with name containing: {}", name);
        List<Patient> patients = patientService.findByFullNameContaining(name);
        model.addAttribute("patients", patients);
        return "patients/index"; 
    }
}
