package com.example.demo.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String username);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :role")
    List<User> findByRoles_Name(@Param("role") String role);
}
