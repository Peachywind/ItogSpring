package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Birth date cannot be null")
    private LocalDate birthDate;

    @NotBlank(message = "Gender cannot be blank")
    private String gender;

    // Поле для полного имени
    @Transient  // Указываем, что это поле не должно храниться в базе данных
    private String fullName;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        // Обновляем fullName при изменении имени
        setFullName(name); // Можете изменить логику по желанию
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullName() {
        return fullName != null ? fullName : name; // Возвращаем имя, если fullName не задано
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
