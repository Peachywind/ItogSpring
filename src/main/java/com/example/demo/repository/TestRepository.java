package com.example.demo.repository;

import com.example.demo.model.Test;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends GenericRepository<Test, Long> {
    List<Test> findByTestTypeContainingIgnoreCase(String testType);
}
