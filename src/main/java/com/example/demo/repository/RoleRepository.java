package com.example.demo.repository;

import com.example.demo.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends GenericRepository<Role, Long> {
    
    @Query("SELECT r FROM Role r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Role> findByNameLike(@Param("name") String name);
    
    Role findByName(String name); // Убедитесь, что этот метод объявлен
}
