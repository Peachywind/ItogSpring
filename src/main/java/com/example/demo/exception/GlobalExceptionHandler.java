package com.example.demo.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException ex, Model model) {
        // Добавление сообщения об ошибке в модель
        model.addAttribute("errorMessage", "Невозможно удалить запись, так как существуют связанные записи. Пожалуйста, удалите связанные данные перед удалением этой записи.");
        return "error/customError";
    }
}
