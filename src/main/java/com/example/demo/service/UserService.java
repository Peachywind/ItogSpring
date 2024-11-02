package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends GenericService<User, Long> {

    public UserService(UserRepository userRepository) {
        super(userRepository);
    }

    public Optional<User> findByUsername(String username) {
        return ((UserRepository) repository).findByUsername(username);
    }

    public List<User> findByUsernameContaining(String username) {
        return ((UserRepository) repository).findByUsernameContainingIgnoreCase(username);
    }
    public List<User> getAllDoctors() {
        return ((UserRepository) repository).findByRoles_Name("DOCTOR");
    }
    
}
