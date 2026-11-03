package com.example.Ticketing_project.repository;


import com.example.Ticketing_project.entity.TicketPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketPassRepository extends JpaRepository<TicketPass, Long> {

    // Crucial for the scanner gate: look up a ticket pass by its unique QR code token
    Optional<TicketPass> findByPassToken(String passToken);
}
