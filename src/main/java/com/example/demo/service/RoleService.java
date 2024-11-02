package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService extends GenericService<Role, Long> {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository; // Инициализируем поле
    }

    public List<Role> findByNameLike(String name) {
        return roleRepository.findByNameLike(name);
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
