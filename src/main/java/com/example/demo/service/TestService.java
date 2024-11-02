package com.example.demo.service;

import com.example.demo.model.Test;
import com.example.demo.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService extends GenericService<Test, Long> {

    public TestService(TestRepository testRepository) {
        super(testRepository);
    }

    public List<Test> findByTestTypeContaining(String testType) {
        return ((TestRepository) repository).findByTestTypeContainingIgnoreCase(testType);
    }
}
