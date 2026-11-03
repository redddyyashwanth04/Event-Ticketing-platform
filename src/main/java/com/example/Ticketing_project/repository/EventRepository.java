package com.example.Ticketing_project.repository;


import com.example.Ticketing_project.entity.Event;
import com.example.Ticketing_project.entity.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Fetch public events catalog filtered by status (e.g., PUBLISHED)
    List<Event> findByStatus(EventStatus status);

    // Fetch all events created by a specific organizer
    List<Event> findByOrganizerId(Long organizerId);
}
