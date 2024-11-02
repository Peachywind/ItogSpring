package com.example.demo.repository;

import com.example.demo.model.Department;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends GenericRepository<Department, Long> {
    List<Department> findByNameContainingIgnoreCase(String name);
}
