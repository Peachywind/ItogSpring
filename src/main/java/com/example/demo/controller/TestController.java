package com.example.demo.controller;

import com.example.demo.model.Test;
import com.example.demo.model.Patient; // Импорт модели пациента
import com.example.demo.service.TestService;
import com.example.demo.service.PatientService; // Импорт сервиса пациента
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
@RequestMapping("/tests")
public class TestController {

    private final TestService testService;
    private final PatientService patientService; // Добавляем зависимость от PatientService

    // Конструктор контроллера
    public TestController(TestService testService, PatientService patientService) {
        this.testService = testService;
        this.patientService = patientService; // Инициализируем PatientService
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("tests", testService.findAll());
        return "tests/index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/new")
    public String newTest(Model model) {
        model.addAttribute("test", new Test());
        model.addAttribute("patients", patientService.findAll()); // Получаем список пациентов
        return "tests/new";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public String create(@ModelAttribute("test") @Valid Test test, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.findAll()); // Передаем список пациентов при ошибке
            return "tests/new";
        }
        testService.save(test);
        return "redirect:/tests";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/edit")
    public String editTest(@PathVariable("id") Long id, Model model) {
        Optional<Test> test = testService.findById(id);
        if (test.isPresent()) {
            model.addAttribute("test", test.get());
            model.addAttribute("patients", patientService.findAll()); // Получаем список пациентов для редактирования
            return "tests/edit";
        } else {
            return "redirect:/tests";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}")
    public String updateTest(@PathVariable("id") Long id, @ModelAttribute("test") @Valid Test test, BindingResult result) {
        if (result.hasErrors()) {
            return "tests/edit";
        }
        testService.update(id, test);
        return "redirect:/tests";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteTest(@PathVariable("id") Long id) {
        testService.deleteById(id);
        return "redirect:/tests";
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/search")
    public String search(@RequestParam("type") String type, Model model) {
        List<Test> tests = testService.findByTestTypeContaining(type);
        model.addAttribute("tests", tests);
        return "tests/index";
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public String showTest(@PathVariable("id") Long id, Model model) {
        Optional<Test> test = testService.findById(id);
        if (test.isPresent()) {
            model.addAttribute("test", test.get());
            return "tests/show";
        } else {
            return "redirect:/tests";
        }
    }
}
