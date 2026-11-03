package com.example.Ticketing_project.repository;


import com.example.Ticketing_project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Spring Data JPA automatically writes the SQL query based on the method name!
    // SELECT * FROM roles WHERE name = ?
    Optional<Role> findByName(String name);
}
