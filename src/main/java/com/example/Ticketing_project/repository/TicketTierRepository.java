package com.example.Ticketing_project.repository;


import com.example.Ticketing_project.entity.TicketTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketTierRepository extends JpaRepository<TicketTier, Long> {

    // Fetch all pricing tiers belonging to a specific event ID
    List<TicketTier> findByEventId(Long eventId);
}
