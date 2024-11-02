package com.example.demo.service;

import com.example.demo.model.Department;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService extends GenericService<Department, Long> {

    public DepartmentService(DepartmentRepository departmentRepository) {
        super(departmentRepository);
    }

    public List<Department> findByNameContaining(String name) {
        return ((DepartmentRepository) repository).findByNameContainingIgnoreCase(name);
    }
}
